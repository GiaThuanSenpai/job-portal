package com.job_portal.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.SeekerDTO;
import com.job_portal.DTO.SocialLinkDTO;
import com.job_portal.models.Industry;
import com.job_portal.models.Seeker;
import com.job_portal.models.Skills;
import com.job_portal.models.SocialLink;
import com.job_portal.repository.IndustryRepository;
import com.job_portal.repository.SeekerRepository;
import com.job_portal.repository.SkillRepository;
import com.job_portal.repository.UserAccountRepository;
import com.social.exceptions.AllExceptions;

@Service
public class SeekerServiceImpl implements ISeekerService {

	@Autowired
	private SeekerRepository seekerRepository;

	@Autowired
	private IndustryRepository industryRepository;

	@Autowired
	private SkillRepository skillRepository;

	@Autowired
	private UserAccountRepository accountRepository;

	@Override
	public boolean deleteSeeker(UUID userId) throws AllExceptions {
		Optional<Seeker> seeker = seekerRepository.findById(userId);

		if (seeker.isEmpty()) {
			throw new AllExceptions("Seeker not exist with id: " + userId);
		}

		seekerRepository.delete(seeker.get());
		return true;
	}

	@Override
	public boolean updateSeeker(SeekerDTO seekerDTO, UUID userId) throws AllExceptions {
	    // Tìm kiếm Seeker theo id
	    Optional<Seeker> existingSeekerOpt = seekerRepository.findById(userId);

	    // Lấy đối tượng Seeker cũ
	    Seeker oldSeeker = existingSeekerOpt.get();
	    boolean isUpdated = false;

	    // Cập nhật các trường cơ bản
	    if (seekerDTO.getAddress() != null) {
	        oldSeeker.setAddress(seekerDTO.getAddress());
	        isUpdated = true;
	    }
	    if (seekerDTO.getGender() != null) {
	        oldSeeker.setGender(seekerDTO.getGender());
	        isUpdated = true;
	    }
	    if (seekerDTO.getDateOfBirth() != null) {
	        oldSeeker.setDateOfBirth(seekerDTO.getDateOfBirth());
	        isUpdated = true;
	    }
	    if (seekerDTO.getPhoneNumber() != null) {
	        oldSeeker.setPhoneNumber(seekerDTO.getPhoneNumber());
	        isUpdated = true;
	    }
	    if (seekerDTO.getDescription() != null) {
	        oldSeeker.setDescription(seekerDTO.getDescription());
	        isUpdated = true;
	    }
	    if (seekerDTO.getEmailContact() != null) {
	        oldSeeker.setEmailContact(seekerDTO.getEmailContact());
	        isUpdated = true;
	    }

	    // Cập nhật Industry
	    if (seekerDTO.getIndustryId() != null) {
	        Optional<Industry> newIndustry = industryRepository.findById(seekerDTO.getIndustryId());
	        if (!newIndustry.get().equals(oldSeeker.getIndustry())) {
	            oldSeeker.setIndustry(newIndustry.get());
	            isUpdated = true;
	        }
	    }

	    // Cập nhật danh sách kỹ năng
	    if (seekerDTO.getSkillIds() != null) {
	        List<Skills> skillsList = new ArrayList<>();
	        for (Integer skillId : seekerDTO.getSkillIds()) {
	            Optional<Skills> skillOpt = skillRepository.findById(skillId);
	            skillsList.add(skillOpt.get());
	        }
	        oldSeeker.setSkills(skillsList);
	        isUpdated = true; 
	    }

	    // Cập nhật danh sách SocialLink
	    if (seekerDTO.getSocialLinks() != null) {
	        List<SocialLink> existingSocialLinks = oldSeeker.getSocialLinks();

	        for (SocialLinkDTO socialLinkDTO : seekerDTO.getSocialLinks()) {
	            boolean found = false;

	            for (SocialLink existingLink : existingSocialLinks) {
	                if (existingLink.getSocialName().equals(socialLinkDTO.getSocialName())) {
	                    existingLink.setLink(socialLinkDTO.getLink());
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	                SocialLink newSocialLink = new SocialLink();
	                newSocialLink.setUserId(userId);
	                newSocialLink.setSocialName(socialLinkDTO.getSocialName());
	                newSocialLink.setLink(socialLinkDTO.getLink());
	                newSocialLink.setSeeker(oldSeeker);
	                existingSocialLinks.add(newSocialLink);
	            }
	        }

	        existingSocialLinks.removeIf(existingLink ->
	            seekerDTO.getSocialLinks().stream()
	                .noneMatch(newLink -> newLink.getSocialName().equals(existingLink.getSocialName()))
	        );

	        isUpdated = true; 
	    }

	    // Lưu lại đối tượng Seeker đã cập nhật nếu có thay đổi
	    if (isUpdated) {
	        seekerRepository.save(oldSeeker);
	    }

	    return isUpdated;
	}

	
	@Override
	public boolean deleteSocialLink(UUID userId, String socialName) throws AllExceptions {
	    // Tìm kiếm Seeker của người dùng
	    Optional<Seeker> seekerOpt = seekerRepository.findById(userId);
	    if (seekerOpt.isEmpty()) {
	        throw new AllExceptions("Seeker not found with userId: " + userId);
	    }
	    
	    Seeker seeker = seekerOpt.get();
	    // Tìm kiếm và xóa SocialLink
	    Iterator<SocialLink> iterator = seeker.getSocialLinks().iterator();
	    while (iterator.hasNext()) {
	        SocialLink socialLink = iterator.next();
	        if (socialLink.getSocialName().equalsIgnoreCase(socialName)) {
	            iterator.remove(); // Xóa SocialLink khỏi danh sách
	            seekerRepository.save(seeker); // Lưu lại Seeker
	            return true; // Đã xóa thành công
	        }
	    }
	    return false; // Không tìm thấy SocialLink
	}



	@Override
	public List<Seeker> searchSeekerByName(String userName) throws AllExceptions {
		try {
			List<Seeker> seekers = seekerRepository.findSeekerByUserName(userName);
			if (seekers.isEmpty()) {
				throw new AllExceptions("Không tìm thấy người tìm viêc nào với tên: " + userName);
			}

			return seekers;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<Seeker> searchSeekerByIndustry(String industryName) throws AllExceptions {
		try {
			List<Seeker> seekers = seekerRepository.findSeekerByIndustryName(industryName);
			if (seekers.isEmpty()) {
				throw new AllExceptions("Không tìm thấy người tìm việc nào với tên ngành: " + industryName);
			}
			return seekers;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public Seeker findSeekerById(UUID userId) throws AllExceptions {
		try {
			// Tìm kiếm công ty dựa trên companyId
			Optional<Seeker> seeker = seekerRepository.findById(userId);

			// Nếu không tìm thấy công ty, ném ra ngoại lệ
			if (!seeker.isPresent()) {
				throw new AllExceptions("Không tìm thấy người tìm việc với ID: " + userId.toString());
			}

			// Trả về công ty nếu tìm thấy
			return seeker.get();
		} catch (Exception e) {
			// Ném ra ngoại lệ nếu có lỗi xảy ra
			throw new AllExceptions(e.getMessage());
		}
	}

}

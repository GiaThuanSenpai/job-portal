package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.Industry;
import com.job_portal.models.Seeker;
import com.job_portal.repository.IndustryRepository;
import com.job_portal.repository.SeekerRepository;
import com.social.exceptions.AllExceptions;

@Service
public class SeekerServiceImpl implements ISeekerService {

	@Autowired
	private SeekerRepository seekerRepository;

	@Autowired
	private IndustryRepository industryRepository;

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
	public boolean updateSeeker(Seeker seeker, UUID userId, Integer industryId) throws AllExceptions {
		// Tìm kiếm Company theo id
		Optional<Seeker> existingSeeker = seekerRepository.findById(userId);

		if (existingSeeker.isEmpty()) {
			throw new AllExceptions("Seeker not exist with id " + userId);
		}

		// Lấy đối tượng Company cũ
		Seeker oldSeeker = existingSeeker.get();
		boolean isUpdated = false;

		// Cập nhật các trường cơ bản
		if (seeker.getAddress() != null) {
			oldSeeker.setAddress(seeker.getAddress());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (seeker.getGender() != null) {
			oldSeeker.setGender(seeker.getAddress());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (seeker.getDateOfBirth() != null) {
			oldSeeker.setDateOfBirth(seeker.getDateOfBirth());
			isUpdated = true;
		}
		// Cập nhật các trường cơ bản
		if (seeker.getPhoneNumber() != null) {
			oldSeeker.setPhoneNumber(seeker.getPhoneNumber());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (seeker.getDescription() != null) {
			oldSeeker.setDescription(seeker.getDescription());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (seeker.getEmailContact() != null) {
			oldSeeker.setEmailContact(seeker.getEmailContact());
			isUpdated = true;
		}

		// Tìm Industry mới dựa trên industryId
		if (industryId != null) {
			Optional<Industry> newIndustry = industryRepository.findById(industryId);
			if (newIndustry.isEmpty()) {
				throw new AllExceptions("Industry not exist with id " + industryId);
			}
			// Cập nhật Industry nếu khác
			if (!newIndustry.get().equals(oldSeeker.getIndustry())) {
				oldSeeker.setIndustry(newIndustry.get());
				isUpdated = true;
			}
		}

		if (isUpdated) {
			seekerRepository.save(oldSeeker);
		}

		return isUpdated;
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

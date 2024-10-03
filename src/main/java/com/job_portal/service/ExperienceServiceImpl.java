package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.ExperienceDTO;
import com.job_portal.models.Company;
import com.job_portal.models.Experience;
import com.job_portal.models.Seeker;
import com.job_portal.repository.ExperienceRepository;
import com.job_portal.repository.SeekerRepository;
import com.social.exceptions.AllExceptions;

@Service
public class ExperienceServiceImpl implements IExperienceService {

	@Autowired
	private ExperienceRepository experienceRepository;
	@Autowired
	private SeekerRepository seekerRepository;
	@Override
	public boolean createExp(ExperienceDTO experienceDTO, UUID userId) {
	    try {
	        Seeker seeker = seekerRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid Seeker ID"));

	        // Build the Experience entity
	        Experience experience = new Experience();
	        experience.setStartDate(experienceDTO.getStartDate());
	        experience.setEndDate(experienceDTO.getEndDate());
	        experience.setIsCurrentJob(experienceDTO.getIsCurrentJob());
	        experience.setJobTitle(experienceDTO.getJobTitle());
	        experience.setCompanyName(experienceDTO.getCompanyName());
	        experience.setDescription(experienceDTO.getDescription());
	        experience.setSeeker(seeker);

	        // Save the Experience entity
	        Experience saveExperience = experienceRepository.save(experience);
	        return saveExperience != null;

	    } catch (IllegalArgumentException e) {
	        // Xử lý lỗi cho ID seeker không hợp lệ
	        System.out.println("Error: " + e.getMessage());
	        return false;

	    } catch (Exception e) {
	        // Xử lý tất cả các ngoại lệ khác
	        System.out.println("Unexpected error: " + e.getMessage());
	        e.printStackTrace(); // In ra chi tiết lỗi
	        return false;
	    }
	}


	@Override
	public boolean deleteExp(Integer expId) throws AllExceptions {
		Optional<Experience> experience = experienceRepository.findById(expId);

		if (experience.isEmpty()) {
			throw new AllExceptions("Seeker not exist with id: " + expId);
		}

		experienceRepository.delete(experience.get());
		return true;
	}

	@Override
	public boolean updateExp(Experience experience, Integer expId, UUID userId) throws AllExceptions {
		Optional<Experience> existingExp = experienceRepository.findById(expId);

		if (existingExp.isEmpty()) {
			throw new AllExceptions("Experience not exist with id " + expId);
		}

		// Lấy đối tượng Company cũ
		Experience oldExp = existingExp.get();
		boolean isUpdated = false;

		// Cập nhật các trường cơ bản
		if (experience.getStartDate() != null) {
			oldExp.setStartDate(experience.getStartDate());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (experience.getEndDate() != null) {
			oldExp.setEndDate(experience.getEndDate());
			isUpdated = true;
		}
		if (experience.getIsCurrentJob() != null) {
			oldExp.setIsCurrentJob(experience.getIsCurrentJob());
			isUpdated = true;
		}

		if (experience.getJobTitle() != null) {
			oldExp.setJobTitle(experience.getJobTitle());
			isUpdated = true;
		}
		if (experience.getCompanyName() != null) {
			oldExp.setCompanyName(experience.getCompanyName());
			isUpdated = true;
		}
		if (experience.getDescription() != null) {
			oldExp.setDescription(experience.getDescription());
			isUpdated = true;
		}


		// Tìm Industry mới dựa trên industryId
		if (userId != null) {
			Optional<Seeker> newSeeker = seekerRepository.findById(userId);
			if (newSeeker.isEmpty()) {
				throw new AllExceptions("Seeker not exist with id " + userId);
			}
			// Cập nhật Industry nếu khác
			if (!newSeeker.get().equals(oldExp.getSeeker())) {
				oldExp.setSeeker(newSeeker.get());
				isUpdated = true;
			}
		}

		if (isUpdated) {
			experienceRepository.save(oldExp);
		}

		return isUpdated;
	}

	@Override
	public List<Experience> searchExpByUserId(UUID userId) throws AllExceptions {
		try {

			List<Experience> experiences = experienceRepository.findExpByUserId(userId);
			if (experiences.isEmpty()) {
				throw new AllExceptions("Không có kinh nghiệm");
			}
			return experiences;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}



}

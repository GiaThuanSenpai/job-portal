package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.EducationDTO;
import com.job_portal.DTO.ExperienceDTO;
import com.job_portal.models.Education;
import com.job_portal.models.Experience;
import com.job_portal.models.JobPost;
import com.job_portal.models.Seeker;
import com.job_portal.repository.EducationRepository;import com.job_portal.repository.ExperienceRepository;
import com.job_portal.repository.SeekerRepository;
import com.social.exceptions.AllExceptions;

@Service
public class EducationServiceImpl implements IEducationService {

	@Autowired
	private EducationRepository educationRepository;
	@Autowired
	private SeekerRepository seekerRepository;

	@Override
	public boolean createEdu(EducationDTO educationDTO, UUID userId) {
		Seeker seeker = seekerRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Seeker ID"));

		// Build the Experience entity
		Education education = new Education();
		education.setCertificateDegreeName(educationDTO.getCertificateDegreeName());
		education.setMajor(educationDTO.getMajor());
		education.setUniversityName(educationDTO.getUniversityName());
		education.setStartDate(educationDTO.getStartDate());
		education.setEndDate(educationDTO.getEndDate());
		education.setGpa(educationDTO.getGpa());
		education.setSeeker(seeker);

		try {
			Education saveEducation = educationRepository.save(education);
			return saveEducation != null;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean deleteEdu(Integer eduId) throws AllExceptions {
		Optional<Education> education = educationRepository.findById(eduId);

		if (education.isEmpty()) {
			throw new AllExceptions("Education not exist");
		}

		educationRepository.delete(education.get());
		return true;
	}

	@Override
	public boolean updateEdu(Education education, Integer eduId, UUID userId) throws AllExceptions {
		Optional<Education> existingEdu = educationRepository.findById(eduId);

		if (existingEdu.isEmpty()) {
			throw new AllExceptions("Education not exist");
		}


		Education oldEdu = existingEdu.get();
		boolean isUpdated = false;

		if (education.getStartDate() != null) {
			oldEdu.setCertificateDegreeName(education.getCertificateDegreeName());
			isUpdated = true;
		}

		if (education.getMajor() != null) {
			oldEdu.setMajor(education.getMajor());
			isUpdated = true;
		}
		if (education.getUniversityName() != null) {
			oldEdu.setUniversityName(education.getUniversityName());
			isUpdated = true;
		}

		if (education.getStartDate() != null) {
			oldEdu.setStartDate(education.getStartDate());
			isUpdated = true;
		}
		if (education.getEndDate() != null) {
			oldEdu.setEndDate(education.getEndDate());
			isUpdated = true;
		}
		if (education.getGpa() != null) {
			oldEdu.setGpa(education.getGpa());
			isUpdated = true;
		}

		if (userId != null) {
			Optional<Seeker> newSeeker = seekerRepository.findById(userId);
			if (newSeeker.isEmpty()) {
				throw new AllExceptions("Seeker not exist");
			}
			// Cập nhật Industry nếu khác
			if (!newSeeker.get().equals(oldEdu.getSeeker())) {
				oldEdu.setSeeker(newSeeker.get());
				isUpdated = true;
			}
		}

		if (isUpdated) {
			educationRepository.save(oldEdu);
		}

		return isUpdated;
	}

	@Override
	public List<Education> searchEduByUserId(UUID userId) throws AllExceptions {
		try {

			List<Education> educations = educationRepository.findEduByUserId(userId);
			if (educations.isEmpty()) {
				throw new AllExceptions("Không có dữ liệu học tập");
			}
			return educations;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

}

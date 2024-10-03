package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.ExperienceDTO;
import com.job_portal.models.Experience;
import com.job_portal.models.JobPost;
import com.social.exceptions.AllExceptions;

public interface IExperienceService {
	public boolean createExp(ExperienceDTO experienceDTO, UUID userId);
	public boolean deleteExp(Integer expId) throws AllExceptions;
	public boolean updateExp(Experience experience,Integer expId, UUID userId) throws AllExceptions;
	public List<Experience> searchExpByUserId(UUID userId) throws AllExceptions;
}

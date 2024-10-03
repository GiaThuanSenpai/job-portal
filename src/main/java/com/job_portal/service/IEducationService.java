package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.EducationDTO;
import com.job_portal.models.Education;
import com.social.exceptions.AllExceptions;

public interface IEducationService {
	public boolean createEdu(EducationDTO educationDTO, UUID userId);
	public boolean deleteEdu(Integer eduId) throws AllExceptions;
	public boolean updateEdu(Education education,Integer eduId, UUID userId) throws AllExceptions;
	public List<Education> searchEduByUserId(UUID userId) throws AllExceptions;
}

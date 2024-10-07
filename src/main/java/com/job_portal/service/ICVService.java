package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.CVDTO;
import com.job_portal.models.CV;
import com.social.exceptions.AllExceptions;

public interface ICVService {
	public boolean createCV(CVDTO cvdto, UUID userId);
	public boolean deleteCV(Integer cvId) throws AllExceptions;
	public boolean updateImg(CVDTO cvdto, Integer cvId) throws AllExceptions;	
	public List<CV> findCVBySeekerId(UUID userId) throws AllExceptions;
}

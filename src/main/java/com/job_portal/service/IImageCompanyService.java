package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.ImageDTO;
import com.job_portal.DTO.JobPostDTO;
import com.job_portal.models.ImageCompany;
import com.job_portal.models.JobPost;
import com.social.exceptions.AllExceptions;

public interface IImageCompanyService {
	public boolean createImg(ImageDTO imageDTO, UUID companyId);
	public boolean deleteImg(Integer imgId) throws AllExceptions;
	public boolean updateImg(ImageDTO imageDTO, Integer imgId) throws AllExceptions;	
	public List<ImageCompany> findImgByCompanyId(UUID companyId) throws AllExceptions;
}

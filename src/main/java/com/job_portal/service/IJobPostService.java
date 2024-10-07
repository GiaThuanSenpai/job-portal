package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.JobPostDTO;
import com.job_portal.models.JobPost;
import com.social.exceptions.AllExceptions;

public interface IJobPostService {
	public boolean createJob(JobPostDTO jobPostDTO, UUID companyId);
	public boolean deleteJob(UUID postId) throws AllExceptions;
	public boolean updateJob(JobPostDTO jobPost, UUID postId) throws AllExceptions;	
	public List<JobPost> searchJobByJobName(String title) throws AllExceptions;
	public List<JobPost> searchJobByExperience(String experience) throws AllExceptions;
	public List<JobPost> searchJobByCity(Integer cityId) throws AllExceptions;
	public List<JobPost> findBySalaryGreaterThanEqual(Long minSalary) throws AllExceptions;
	public List<JobPost> findBySalaryLessThanEqual(Long maxSalary) throws AllExceptions;
	public List<JobPost> findBySalaryBetween(Long minSalary, Long maxSalary) throws AllExceptions;
	public boolean approveJob(UUID postId);
}

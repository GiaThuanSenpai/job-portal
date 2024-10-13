package com.job_portal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.ApplyJob;
import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.JobPost;
import com.job_portal.models.Seeker;
import com.job_portal.repository.ApplyJobRepository;
import com.job_portal.repository.JobPostRepository;
import com.job_portal.repository.SeekerRepository;
import com.social.exceptions.AllExceptions;

@Service
public class ApplyJobServiceImpl implements IApplyJobService {

	@Autowired
	ApplyJobRepository applyJobRepository;
	@Autowired
	JobPostRepository jobPostRepository;
	@Autowired
	SeekerRepository seekerRepository;

	@Override
	public boolean createApplyJob(ApplyJob applyJob) throws AllExceptions {
		ApplyJob saveApplyJob = applyJobRepository.save(applyJob);
		return saveApplyJob != null;

	}

	@Override
	public boolean updateApplyJob(ApplyJob applyJob) throws AllExceptions {

		// Kiểm tra tồn tại của JobPosts và SeekerProfile
		Optional<JobPost> jobPost = jobPostRepository.findById(applyJob.getPostId());
		if (!jobPost.isPresent()) {
			throw new IllegalArgumentException("JobPost không tồn tại với postId: " + applyJob.getPostId());
		}

		Optional<Seeker> seeker = seekerRepository.findById(applyJob.getUserId());
		if (!seeker.isPresent()) {
			throw new IllegalArgumentException("SeekerProfile không tồn tại với userId: " + applyJob.getUserId());
		}
		try {
			ApplyJob saveApplyJob = applyJobRepository.save(applyJob);
			return saveApplyJob != null;
		} catch (Exception e) {
			return false;
		}
	}

}

package com.job_portal.service;

import com.job_portal.models.ApplyJob;
import com.social.exceptions.AllExceptions;

public interface IApplyJobService {
	public boolean createApplyJob(ApplyJob applyJob) throws AllExceptions;
	public boolean updateApplyJob(ApplyJob applyJob) throws AllExceptions;
}

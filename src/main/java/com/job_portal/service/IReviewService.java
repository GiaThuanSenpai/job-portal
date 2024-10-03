package com.job_portal.service;

import java.util.List;
import java.util.UUID;

import com.job_portal.models.Review;
import com.job_portal.models.Seeker;
import com.social.exceptions.AllExceptions;

public interface IReviewService {
	public boolean createReview(Seeker seeker, UUID companyId, Review req) throws AllExceptions;
	public List<Review> findReviewByCompanyId(UUID companyId) throws AllExceptions;
}

package com.job_portal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.job_portal.models.Company;
import com.job_portal.models.JobPost;
import com.job_portal.models.Review;
import com.job_portal.models.Seeker;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.ReviewRepository;
import com.social.exceptions.AllExceptions;

public class ReviewServiceImpl implements IReviewService {

	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	CompanyRepository companyRepository;
	@Override
	public boolean createReview(Seeker seeker, UUID companyId, Review req) throws AllExceptions {

	    Review review = new Review();
	    Optional<Company> companyOptional = companyRepository.findById(companyId);

	    if (companyOptional.isPresent()) {
	        Company company = companyOptional.get();
	        review.setCompany(company);
	        review.setMessage(req.getMessage());
	        review.setStar(req.getStar());
	        review.setSeeker(seeker);
	        review.setCreateDate(LocalDateTime.now());

	        try {
	            Review savedReview = reviewRepository.save(review);
	            company.getReviews().add(savedReview);
	            companyRepository.save(company); 
	            return savedReview != null;
	        } catch (Exception e) {
	            return false;
	        }
	    } else {
	        throw new AllExceptions("Company not found with ID: " + companyId);
	    }
	}


	@Override
	public List<Review> findReviewByCompanyId(UUID companyId) throws AllExceptions {
		// TODO Auto-generated method stub
		return null;
	}

}

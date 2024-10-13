package com.job_portal.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.Company;
import com.job_portal.models.JobPost;
import com.job_portal.models.Review;
import com.job_portal.models.Seeker;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.ReviewRepository;
import com.social.exceptions.AllExceptions;

@Service
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

			Review savedReview = reviewRepository.save(review);
			company.getReviews().add(savedReview);
			companyRepository.save(company);
		}
		return false;
	}

	@Override
	public List<Review> findReviewByCompanyId(UUID companyId) throws AllExceptions {
		try {
			List<Review> reviews = reviewRepository.findReviewByCompanyId(companyId);
			if (reviews.isEmpty()) {
				throw new AllExceptions("Không tìm thấy đánh giá nào");
			}

			return reviews;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

}

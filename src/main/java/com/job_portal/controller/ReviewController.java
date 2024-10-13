package com.job_portal.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.JobPostDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.JobPost;
import com.job_portal.models.Review;
import com.job_portal.models.Seeker;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.JobPostRepository;
import com.job_portal.repository.ReviewRepository;
import com.job_portal.repository.SeekerRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.IJobPostService;
import com.job_portal.service.IReviewService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	ReviewRepository reviewRepository;

	@Autowired
	IReviewService reviewService;
	@Autowired
	SeekerRepository seekerRepository;


	@Autowired
	private UserAccountRepository userAccountRepository;

	@GetMapping("/get-all")
	public ResponseEntity<List<Review>> getReview() {
		List<Review> reviews = reviewRepository.findAll();
		return new ResponseEntity<>(reviews, HttpStatus.OK);
	}

	@PostMapping("/create-review/{companyId}")
	public ResponseEntity<String> createReview(
			@RequestHeader("Authorization") String jwt,
			@RequestBody Review req,
			@PathVariable("companyId") UUID companyId) throws AllExceptions {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);
		Optional<Seeker> seeker = seekerRepository.findById(user.get().getUserId());
		
		boolean isCreated = reviewService.createReview(seeker.get(),companyId, req);
		if (isCreated) {
			return new ResponseEntity<>("Đánh giá thành công", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Đánh giá thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/findReviewByCompanyId/{companyId}")
	public ResponseEntity<Object> searchReviewByCompanyId(@PathVariable("companyId") UUID companyId) {
		try {
			List<Review> reviews = reviewService.findReviewByCompanyId(companyId);
			return ResponseEntity.ok(reviews);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}
}

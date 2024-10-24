package com.job_portal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job_portal.DTO.CompanyDTO;
import com.job_portal.DTO.DailyJobCount;
import com.job_portal.DTO.JobPostDTO;
import com.job_portal.DTO.SeekerDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.Company;
import com.job_portal.models.JobPost;
import com.job_portal.models.Seeker;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.JobPostRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.ICompanyService;
import com.job_portal.service.IJobPostService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/job-post")
public class JobPostController {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	JobPostRepository jobPostRepository;

	@Autowired
	IJobPostService jobPostService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@GetMapping("/get-all")
	public ResponseEntity<List<JobPost>> getJob() {
		List<JobPost> jobs = jobPostRepository.findAll();
		return new ResponseEntity<>(jobs, HttpStatus.OK);
	}

	@GetMapping("/get-job-approve")
	public ResponseEntity<List<JobPost>> getJobApprove() {
		List<JobPost> res = jobPostService.findByIsApproveTrue();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PostMapping("/create-job")
	public ResponseEntity<String> createJobPost(@RequestHeader("Authorization") String jwt,
			@RequestBody JobPostDTO jobPostDTO) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		boolean isCreated = jobPostService.createJob(jobPostDTO, user.get().getUserId());
		if (isCreated) {
			return new ResponseEntity<>("Công việc được tạo thành công. Chờ Admin phê duyệt", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Công việc tạo thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/approve/{postId}")
	public ResponseEntity<String> approveJobPost(@PathVariable UUID postId) {
		boolean isApproved = jobPostService.approveJob(postId);
		if (isApproved) {
			return ResponseEntity.ok("Chấp thuận thành công");
		} else {
			return ResponseEntity.status(404).body("Không thể tìm thấy công việc");
		}
	}

	@PutMapping("/update-job/{postId}")
	public ResponseEntity<String> updateJobPost(@RequestHeader("Authorization") String jwt,
			@RequestBody JobPostDTO jobPost, @PathVariable("postId") UUID postId) throws AllExceptions {
		Optional<JobPost> oldJobPost = jobPostRepository.findById(postId);
		if (oldJobPost.get().isApprove() == false) {
			boolean isUpdated = jobPostService.updateJob(jobPost, postId);
			if (isUpdated) {
				return new ResponseEntity<>("Cập nhật thành công", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Cập nhật thất bại", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Bài viết đã được chấp thuận, không được thay đổi", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete-job/{postId}")
	public ResponseEntity<String> deleteJob(@PathVariable("postId") UUID postId) {
		try {
			boolean isDeleted = jobPostService.deleteJob(postId);
			if (isDeleted) {
				return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Xóa thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search-by-job-name")
	public ResponseEntity<Object> searchJobByJobName(@RequestHeader("Authorization") String jwt,
			@RequestParam("title") String title) {
		try {
			String email = JwtProvider.getEmailFromJwtToken(jwt);
			Optional<UserAccount> user = userAccountRepository.findByEmail(email);
			UUID userId = null;
			if (user.get().getUserId() != null) {
				userId = user.get().getUserId();
			}

			List<JobPost> jobs = jobPostService.searchJobByJobName(title, userId);

			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/search-by-experience")
	public ResponseEntity<Object> searchJobByExperience(@RequestParam("experience") String experience) {
		try {
			List<JobPost> jobs = jobPostService.searchJobByExperience(experience);
			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/search-by-city")
	public ResponseEntity<Object> searchJobByCity(@RequestParam("city") Integer city) {
		try {
			List<JobPost> jobs = jobPostService.searchJobByCity(city);
			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/min-salary/{minSalary}")
	public ResponseEntity<Object> findBySalaryGreaterThanEqual(@PathVariable("minSalary") Long minSalary) {
		try {
			List<JobPost> jobs = jobPostService.findBySalaryGreaterThanEqual(minSalary);
			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/max-salary/{maxSalary}")
	public ResponseEntity<Object> findBySalaryLessThanEqual(@PathVariable("maxSalary") Long maxSalary) {
		try {
			List<JobPost> jobs = jobPostService.findBySalaryLessThanEqual(maxSalary);
			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/salary-between")
	public ResponseEntity<Object> findBySalaryBetween(@RequestParam Long minSalary, @RequestParam Long maxSalary) {
		try {
			List<JobPost> jobs = jobPostService.findBySalaryBetween(minSalary, maxSalary);
			return ResponseEntity.ok(jobs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/findJob/{postId}")
	public ResponseEntity<JobPost> getJobById(@PathVariable("postId") UUID postId) throws AllExceptions {
		try {
			JobPost jobPost = jobPostService.searchJobByPostId(postId);
			return new ResponseEntity<>(jobPost, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/count-new-jobs-per-day")
	public List<DailyJobCount> countNewJobsPerDay(@RequestParam String startDate, @RequestParam String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);

		LocalDateTime startDateTime = start.atStartOfDay();
		LocalDateTime endDateTime = end.atTime(23, 59, 59);

		return jobPostService.getDailyJobPostCounts(startDateTime, endDateTime);
	}

	@PostMapping("/recommend-jobs")
	public ResponseEntity<List<JobPost>> getJobRecommendations(@RequestHeader("Authorization") String jwt) {
	    // Lấy email từ JWT
	    String email = JwtProvider.getEmailFromJwtToken(jwt);

	    // Tìm người dùng bằng email
	    Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email);
	    if (!userOptional.isPresent()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    UserAccount user = userOptional.get();
	    UUID userId = user.getUserId();

	    // Tạo body để gửi đến API Python
	    Map<String, String> requestBody = new HashMap<>();
	    requestBody.put("userId", userId.toString());
	    System.out.println("User ID sent to Python API: " + userId);

	    // Gửi yêu cầu đến API Python
	    String apiUrl = "http://localhost:5000/recommend-jobs";
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json; charset=UTF-8");

	    // Sử dụng ObjectMapper để chuyển đổi requestBody thành JSON
	    ObjectMapper objectMapper = new ObjectMapper();
	    String jsonRequestBody;
	    try {
	        jsonRequestBody = objectMapper.writeValueAsString(requestBody);
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(null);
	    }

	    HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);

	    try {
	        // Gửi yêu cầu đến API Python
	        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
	        
	        // Chuyển đổi JSON Response thành JsonNode
	        JsonNode jsonResponse = objectMapper.readTree(response.getBody());

	        // Tạo danh sách JobPost
	        List<JobPost> jobs = new ArrayList<>();

	        // Duyệt qua từng đối tượng trong JsonNode và thiết lập từng giá trị cho JobPost
	        for (JsonNode jobNode : jsonResponse) {
	            JobPost job = new JobPost();
	            job.setDescription(jobNode.get("description").asText(null));
	            job.setExperience(jobNode.get("experience").asText(null));
	            job.setLocation(jobNode.get("location").asText(null));
	            job.setPostId(UUID.fromString(jobNode.get("postId").asText()));
	            job.setSalary(jobNode.get("salary").asLong());
	            job.setTitle(jobNode.get("title").asText(null));
	            job.setTypeOfWork(jobNode.get("typeOfWork").asText(null)); 

	            jobs.add(job);
	        }

	        return ResponseEntity.ok(jobs); // Trả về danh sách việc làm
	    } catch (JsonProcessingException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(null);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(null);
	    }
	}
}

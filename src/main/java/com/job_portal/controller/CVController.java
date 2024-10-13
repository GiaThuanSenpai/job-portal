package com.job_portal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.CVDTO;
import com.job_portal.DTO.ImageDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.CV;
import com.job_portal.models.ImageCompany;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.CVRepository;
import com.job_portal.repository.ExperienceRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.ICVService;
import com.job_portal.service.IExperienceService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/cv")
public class CVController {
	@Autowired
	CVRepository cvRepository;

	@Autowired
	ICVService cvService;

	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@GetMapping("/get-all")
	public ResponseEntity<List<CV>> getCV() {
		List<CV> cvs = cvRepository.findAll();
		return new ResponseEntity<>(cvs, HttpStatus.OK);
	}
	
	@PostMapping("/create-cv")
	public ResponseEntity<String> createCV(@RequestHeader("Authorization") String jwt,
			@RequestBody CVDTO cvdto) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		boolean isCreated = cvService.createCV(cvdto, user.get().getUserId());
		if (isCreated) {
			return new ResponseEntity<>("Tạo CV thành công", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Tạo CV thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@DeleteMapping("/delete-cv/{cvId}")
	public ResponseEntity<String> deleteCV(@PathVariable("cvId") Integer cvId) {
		try {
			boolean isDeleted = cvService.deleteCV(cvId);
			if (isDeleted) {
				return new ResponseEntity<>("Xóa CV thành công", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Xóa CV thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/searchCV")
	public ResponseEntity<Object> searchCV(@RequestHeader("Authorization") String jwt) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);
		try {
			List<CV> cvs = cvService.findCVBySeekerId(user.get().getUserId());
			return ResponseEntity.ok(cvs);
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

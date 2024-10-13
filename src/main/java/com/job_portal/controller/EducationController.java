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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.EducationDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.Education;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.EducationRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.IEducationService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/education")
public class EducationController {
	@Autowired
	EducationRepository educationRepository;

	@Autowired
	IEducationService educationService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@GetMapping("/get-all")
	public ResponseEntity<List<Education>> getExperience() {
		List<Education> educations = educationRepository.findAll();
		return new ResponseEntity<>(educations, HttpStatus.OK);
	}

	@PostMapping("/create-education")
	public ResponseEntity<String> createEducation(@RequestHeader("Authorization") String jwt,
			@RequestBody EducationDTO educationDTO) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		boolean isCreated = educationService.createEdu(educationDTO, user.get().getUserId());
		if (isCreated) {
			return new ResponseEntity<>("Education created successfully.", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Failed to create Education.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update-education/{educationId}")
	public ResponseEntity<String> updateEducation(@RequestHeader("Authorization") String jwt,
			@RequestBody EducationDTO educationDTO, @PathVariable("educationId") Integer educationId) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		Optional<Education> reqEdu = educationRepository.findById(educationId);
		if (reqEdu.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			Education newEdu = new Education();
			newEdu.setCertificateDegreeName(educationDTO.getCertificateDegreeName());
			newEdu.setMajor(educationDTO.getMajor());
			newEdu.setUniversityName(educationDTO.getUniversityName());
			newEdu.setStartDate(educationDTO.getStartDate());
			newEdu.setEndDate(educationDTO.getEndDate());
			newEdu.setGpa(educationDTO.getGpa());;
			boolean isUpdated = educationService.updateEdu(newEdu, reqEdu.get().getEducationId(),
					user.get().getSeeker().getUserId());
			if (isUpdated) {
				return new ResponseEntity<>("Update Education success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Update Education failed", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete-education/{educationId}")
	public ResponseEntity<String> deleteEducation(@PathVariable("educationId") Integer educationId) {
		try {
			boolean isDeleted = educationService.deleteEdu(educationId);
			if (isDeleted) {
				return new ResponseEntity<>("Education deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Education deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<Object> searchEduByUserId(@RequestHeader("Authorization") String jwt) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);
		try {
			List<Education> edus = educationService.searchEduByUserId(user.get().getUserId());
			return ResponseEntity.ok(edus);
		} catch (AllExceptions e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}
}

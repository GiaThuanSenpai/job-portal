package com.job_portal.controller;

import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.ApplyJobDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.ApplyJob;

import com.job_portal.models.UserAccount;
import com.job_portal.repository.ApplyJobRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.IApplyJobService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/apply-job")
public class ApplyJobController {
	@Autowired
	ApplyJobRepository applyJobRepository;
	@Autowired
	IApplyJobService applyJobService;
	@Autowired
	UserAccountRepository userAccountRepository;

	@PostMapping("/create-apply/{postId}")
	public ResponseEntity<String> createApply(@RequestBody ApplyJobDTO applyDTO, @RequestHeader("Authorization") String jwt,
			@PathVariable("postId") UUID postId) throws AllExceptions {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		UserAccount user = userAccountRepository.findByEmail(email);
		ApplyJob apply = convertToEntity(applyDTO, user.getUserId(), postId );
		boolean isCreated = applyJobService.createApplyJob(apply);
		if (isCreated) {
			return new ResponseEntity<>("Apply job successfully.", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Failed to apply job.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/update-apply/{postId}")
	public ResponseEntity<String> updateApply(@RequestBody ApplyJobDTO applyDTO, @RequestHeader("Authorization") String jwt,
			@PathVariable("postId") UUID postId) throws AllExceptions {
		
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		UserAccount user = userAccountRepository.findByEmail(email);
		ApplyJob apply = convertToEntity(applyDTO, user.getUserId(), postId);
		boolean isCreated = applyJobService.updateApplyJob(apply);
		if (isCreated) {
			return new ResponseEntity<>("Update successfully.", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Failed to update.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@GetMapping("/get-all")
	public ResponseEntity<List<ApplyJob>> getApply() {
		List<ApplyJob> apply = applyJobRepository.findAll();
		return new ResponseEntity<>(apply, HttpStatus.OK);
	}
	
	private ApplyJob convertToEntity(ApplyJobDTO applyDTO, UUID userId, UUID postId) {
        ApplyJob apply = new ApplyJob();
        apply.setPostId(postId);
        apply.setUserId(userId);
        apply.setPathCV(applyDTO.getPathCV());
        apply.setApplyDate(LocalDateTime.now());
        apply.setFullName(applyDTO.getFullName());
        apply.setDescription(applyDTO.getDescription());
        apply.setEmail(applyDTO.getEmail());
        apply.setSave(false);
        return apply;
    }

}

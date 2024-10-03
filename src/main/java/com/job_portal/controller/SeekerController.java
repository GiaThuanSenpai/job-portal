package com.job_portal.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.SeekerDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.Seeker;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.SeekerRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.ISeekerService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/seeker")
public class SeekerController {

	@Autowired
	private SeekerRepository seekerRepository;

	@Autowired
	private ISeekerService seekerService;
	@Autowired
	private UserAccountRepository userAccountRepository;

	@GetMapping("/get-all")
	public ResponseEntity<List<Seeker>> getSeeker() {
		List<Seeker> seekers = seekerRepository.findAll();
		return new ResponseEntity<>(seekers, HttpStatus.OK);
	}

	@GetMapping("/searchByName")
	public ResponseEntity<Object> searchSeekersByName(@RequestParam("userName") String userName) {
		try {
			List<Seeker> seekers = seekerService.searchSeekerByName(userName);
			return ResponseEntity.ok(seekers);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/searchByIndustry")
	public ResponseEntity<Object> searchSeekersByIndustry(@RequestParam("industryName") String industryName) {
		try {
			List<Seeker> seekers = seekerService.searchSeekerByIndustry(industryName);
			return ResponseEntity.ok(seekers);
		} catch (AllExceptions e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}
	
	@PutMapping("/update-seeker")
	public ResponseEntity<String> updateSeeker(@RequestHeader("Authorization") String jwt, @RequestBody SeekerDTO seeker) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		UserAccount user = userAccountRepository.findByEmail(email);
		
		Optional<Seeker> reqSeeker = seekerRepository.findById(user.getUserId());
		if (reqSeeker.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			Seeker newSeeker = new Seeker();
			newSeeker.setAddress(seeker.getAddress());
			newSeeker.setGender(seeker.getGender());
			newSeeker.setDateOfBirth(seeker.getDateOfBirth());
			newSeeker.setAddress(seeker.getAddress());
			newSeeker.setPhoneNumber(seeker.getPhoneNumber());
			newSeeker.setEmailContact(seeker.getAddress());
			newSeeker.setDescription(seeker.getDescription());
			newSeeker.setEmailContact(seeker.getEmailContact());
			boolean isUpdated = seekerService.updateSeeker(newSeeker, reqSeeker.get().getUserId(), seeker.getIndustryId());
			if (isUpdated) {
				return new ResponseEntity<>("Update Seeker success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Update Seeker failed", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}

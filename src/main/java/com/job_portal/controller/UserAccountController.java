package com.job_portal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.DailyAccountCount;
import com.job_portal.DTO.DateRangeDTO;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.IUserAccountService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/users")
public class UserAccountController {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private IUserAccountService userAccountService;

	@GetMapping("/get-all")
	public ResponseEntity<List<UserAccount>> getUsers() {
		List<UserAccount> users = userAccountRepository.findAll();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserAccount> getUserById(@PathVariable("userId") UUID userId) throws AllExceptions {
		try {
			Optional<UserAccount> user = userAccountRepository.findById(userId);
			return user.isPresent() ? new ResponseEntity<>(user.get(), HttpStatus.OK)
					: new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update-user")
	public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String jwt,
			@RequestBody UserAccount user) {
		UserAccount reqUser = userAccountService.findUserByJwt(jwt);
		if (reqUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		try {
			boolean isUpdated = userAccountService.updateUser(user, reqUser.getUserId());
			if (isUpdated) {
				return new ResponseEntity<>("Update user success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Update user failed", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete-user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") UUID userId) {
		try {
			boolean isDeleted = userAccountService.deleteUser(userId);
			if (isDeleted) {
				return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("User deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/search")
	public List<UserAccount> searchUser(@RequestParam("query") String query) {
		List<UserAccount> users = userAccountService.searchUser(query);
		return users;
	}

	@GetMapping("/profile")
	public ResponseEntity<UserAccount> getUserFromToken(@RequestHeader("Authorization") String jwt) {
		UserAccount user = userAccountService.findUserByJwt(jwt);
		if (user == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/count-new-accounts-per-day")
	public List<DailyAccountCount> countNewAccountsPerDay(@RequestParam String startDate,
			@RequestParam String endDate) {
		LocalDate start = LocalDate.parse(startDate);
		LocalDate end = LocalDate.parse(endDate);

		LocalDateTime startDateTime = start.atStartOfDay();
		LocalDateTime endDateTime = end.atTime(23, 59, 59); 

		return userAccountService.getDailyAccountCounts(startDateTime, endDateTime);
	}
}

package com.job_portal.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.LoginDTO;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.Industry;
import com.job_portal.models.Seeker;
import com.job_portal.models.UserAccount;
import com.job_portal.models.UserType;
import com.job_portal.repository.CityRepository;
import com.job_portal.repository.IndustryRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.repository.UserTypeRepository;
import com.job_portal.response.AuthResponse;
import com.job_portal.service.AccountDetailServiceImpl;
import com.job_portal.utils.EmailUtil;
import com.job_portal.utils.OtpUtil;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private AccountDetailServiceImpl accountDetailService;

	@Autowired
	private OtpUtil otpUtil;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private IndustryRepository industryRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private UserTypeRepository userTypeRepository;

	@PostMapping("/signup")
	public AuthResponse createUserAccount(@RequestBody UserAccount userAccount) throws Exception {
		UserAccount isExist = userAccountRepository.findByEmail(userAccount.getEmail());
		if (isExist != null) {
			throw new Exception("This email is already used with another account");
		}
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(userAccount.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send OTP. Please try again");
		}

		UserType userType = userTypeRepository.findById(userAccount.getUserType().getUserTypeId())
				.orElseThrow(() -> new Exception("Invalid user type"));
		UserAccount newUser = new UserAccount();
		newUser.setUserId(UUID.randomUUID());
		newUser.setUserType(userType);
		newUser.setActive(false); 
		newUser.setEmail(userAccount.getEmail());
		newUser.setPassword(passwordEncoder.encode(userAccount.getPassword()));
		newUser.setUserName(userAccount.getUserName());
		newUser.setCreateDate(LocalDateTime.now());
		newUser.setOtp(otp);
		newUser.setOtpGeneratedTime(LocalDateTime.now());

		UserAccount savedUser = userAccountRepository.save(newUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());

		String token = JwtProvider.generateToken(authentication);
		AuthResponse res = new AuthResponse(token, "Register Success");

		return res;
	}

	@PutMapping("/verify-account")
	public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
		UserAccount user = userAccountRepository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
		}
		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (2 * 60)) {

			user.setActive(true);
			user.setOtp(null);
			user.setOtpGeneratedTime(null);

			if (user.getUserType().getUserTypeId() == 2) {
				Integer defaultIndustryId = 1; 
				Optional<Industry> defaultIndustryOpt = industryRepository.findById(defaultIndustryId);
				if (defaultIndustryOpt.isEmpty()) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Default industry not found.");
				}
				Industry defaultIndustry = defaultIndustryOpt.get();
				Seeker seeker = new Seeker();
				seeker.setUserAccount(user);
				seeker.setIndustry(defaultIndustry);
				user.setSeeker(seeker);
				userAccountRepository.save(user);	
			}
			else if(user.getUserType().getUserTypeId() == 3) {
				Integer defaultIndustryId = 1; 
				Optional<Industry> defaultIndustryOpt = industryRepository.findById(defaultIndustryId);
				if (defaultIndustryOpt.isEmpty()) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Default industry not found.");
				}
				
				Integer defaultCityId = 0; 
				Optional<City> defaultCityOpt = cityRepository.findById(defaultCityId);
				if (defaultCityOpt.isEmpty()) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Default City not found.");
				}
				Industry defaultIndustry = defaultIndustryOpt.get();
				City defaultCity = defaultCityOpt.get();
				Company company = new Company();
				company.setUserAccount(user);
				company.setIndustry(defaultIndustry);
				company.setCity(defaultCity);
				user.setCompany(company);
				userAccountRepository.save(user);
			}
			return ResponseEntity.ok("OTP verified and account activated");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please regenerate otp and try again");
		}
	}

	@PutMapping("/regenerate-otp")
	public String regenerateOtp(@RequestParam String email) {
		UserAccount user = userAccountRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User not found with email: " + email);
		}
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(email, otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send OTP. Please try again");
		}
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userAccountRepository.save(user);
		return "Email send... please verify";
	}

	@PostMapping("/login")
	public AuthResponse signin(@RequestBody LoginDTO login) {
		AuthResponse res;
		UserAccount user = userAccountRepository.findByEmail(login.getEmail());
		if (!user.isActive()) {
			return res = new AuthResponse("", "Your account is not verified");
		}
		Authentication authentication = authenticate(login.getEmail(), login.getPassword());
		String token = JwtProvider.generateToken(authentication);
		user.setLastLogin(LocalDateTime.now());
		userAccountRepository.save(user);
		res = new AuthResponse(token, "Login Success");

		return res;
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = accountDetailService.loadUserByUsername(email);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username");

		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Password not matched");

		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	}
}

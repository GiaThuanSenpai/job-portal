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
	public ResponseEntity<String> createUserAccount(@RequestBody UserAccount userAccount) throws Exception {
		UserAccount isExist = userAccountRepository.findByEmail(userAccount.getEmail());
		if (isExist != null) {
			throw new Exception("Email này đã được sử dụng ở tài khoản khác");
		}
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(userAccount.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Không thể gửi OTP, vui lòng thử lại");
		}

		Optional<UserType> userType = userTypeRepository.findById(userAccount.getUserType().getUserTypeId());
		UserAccount newUser = new UserAccount();
		newUser.setUserId(UUID.randomUUID());
		newUser.setUserType(userType.get());
		newUser.setActive(false);
		newUser.setEmail(userAccount.getEmail());
		newUser.setPassword(passwordEncoder.encode(userAccount.getPassword()));
		newUser.setUserName(userAccount.getUserName());
		newUser.setCreateDate(LocalDateTime.now());
		newUser.setOtp(otp);
		newUser.setOtpGeneratedTime(LocalDateTime.now());

//		UserAccount savedUser = userAccountRepository.save(newUser);
//
//		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
//				savedUser.getPassword());
//
//		String token = JwtProvider.generateToken(authentication);
//		AuthResponse res = new AuthResponse(token, "Register Success");

		userAccountRepository.save(newUser);

		return ResponseEntity.ok("Vui lòng check email đã nhận mã đăng ký");
	}

	@PutMapping("/verify-account")
	public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
		UserAccount user = userAccountRepository.findByEmail(email);

		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < (2 * 60)) {

			user.setActive(true);
			user.setOtp(null);
			user.setOtpGeneratedTime(null);

			if (user.getUserType().getUserTypeId() == 2) {
				Integer defaultIndustryId = 1;
				Optional<Industry> defaultIndustryOpt = industryRepository.findById(defaultIndustryId);

				Industry defaultIndustry = defaultIndustryOpt.get();
				Seeker seeker = new Seeker();
				seeker.setUserAccount(user);
				seeker.setIndustry(defaultIndustry);
				user.setSeeker(seeker);
				userAccountRepository.save(user);
			} else if (user.getUserType().getUserTypeId() == 3) {
				Integer defaultIndustryId = 1;
				Optional<Industry> defaultIndustryOpt = industryRepository.findById(defaultIndustryId);


				Integer defaultCityId = 0;
				Optional<City> defaultCityOpt = cityRepository.findById(defaultCityId);

				Industry defaultIndustry = defaultIndustryOpt.get();
				City defaultCity = defaultCityOpt.get();
				Company company = new Company();
				company.setUserAccount(user);
				company.setIndustry(defaultIndustry);
				company.setCity(defaultCity);
				user.setCompany(company);
				userAccountRepository.save(user);
			}
			return ResponseEntity.ok("Đăng ký tài khoản thành công");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xác thực OPT thất bại, vui lòng nhập lại email");
		}
	}

	@PutMapping("/regenerate-otp")
	public String regenerateOtp(@RequestParam String email) {
		UserAccount user = userAccountRepository.findByEmail(email);
//		if (user == null) {
//			throw new RuntimeException("User not found with email: " + email);
//		}
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(email, otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Không thể gửi email, vui lòng thử lại");
		}
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userAccountRepository.save(user);
		return "Vui lòng check email đã nhận mã đăng ký";
	}

	@PostMapping("/login")
	public AuthResponse signin(@RequestBody LoginDTO login) {
		AuthResponse res;
		UserAccount user = userAccountRepository.findByEmail(login.getEmail());
		if (!user.isActive()) {
			return res = new AuthResponse("", "Tài khoản của bạn chưa được xác thực");
		}
		Authentication authentication = authenticate(login.getEmail(), login.getPassword());
		String token = JwtProvider.generateToken(authentication);
		user.setLastLogin(LocalDateTime.now());
		userAccountRepository.save(user);
		res = new AuthResponse(token, "Đăng nhập thành công");

		return res;
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = accountDetailService.loadUserByUsername(email);
		if (userDetails == null) {
			throw new BadCredentialsException("Tài khoản không hợp lệ");

		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Password không đúng");

		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

	}
}

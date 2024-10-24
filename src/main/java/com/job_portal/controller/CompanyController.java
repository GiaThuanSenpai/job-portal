package com.job_portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.CompanyDTO;

import com.job_portal.config.JwtProvider;
import com.job_portal.models.ApplyJob;
import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.JobPost;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.ApplyJobRepository;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.ICompanyService;
import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	ICompanyService companyService;

	@Autowired
	private UserAccountRepository userAccountRepository;
	

	@GetMapping("/get-all")
	public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
	    List<CompanyDTO> res = companyRepository.findCompaniesWithSavedApplications();
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/find-all")
	public ResponseEntity<List<Company>> findAllCompanies() {
	    List<Company> res = companyRepository.findAll();
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/update-company")
	public ResponseEntity<String> updateCompany(@RequestHeader("Authorization") String jwt,
			@RequestBody CompanyDTO company) throws AllExceptions {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		Optional<Company> reqCompany = companyRepository.findById(user.get().getUserId());
		if (reqCompany.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Company newCompany = new Company();
		newCompany.setCompanyName(company.getCompanyName());
		newCompany.setAddress(company.getAddress());
		newCompany.setDescription(company.getDescription());
		newCompany.setLogo(company.getLogo());
		newCompany.setContact(company.getContact());
		newCompany.setEmail(company.getEmail());
		newCompany.setEstablishedTime(company.getEstablishedDate());

		boolean isUpdated = companyService.updateCompany(newCompany, reqCompany.get().getCompanyId(),
				company.getIndustryId(), company.getCityId());
		if (isUpdated) {
			return new ResponseEntity<>("Cập nhật thông tin thành công", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Cập nhật thông tin thất bại", HttpStatus.BAD_REQUEST);
		}
	}


	@GetMapping("/searchByName")
	public ResponseEntity<Object> searchCompaniesByName(@RequestParam("companyName") String companyName) {
		try {
			List<Company> companies = companyService.searchCompaniesByName(companyName);
			return ResponseEntity.ok(companies);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/searchByCity")
	public ResponseEntity<Object> searchCompaniesByCity(@RequestParam("cityName") String cityName) {
		try {
			List<Company> companies = companyService.searchCompaniesByCity(cityName);
			return ResponseEntity.ok(companies);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service khi không tìm thấy công ty
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@GetMapping("/searchByIndustry")
	public ResponseEntity<Object> searchCompaniesByIndustry(@RequestParam("industryName") String industryName) {
		try {
			List<Company> companies = companyService.searchCompaniesByIndustry(industryName);
			return ResponseEntity.ok(companies);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service khi không tìm thấy công ty
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}

	@PutMapping("/follow/{companyId}")
	public ResponseEntity<Map<String, Object>> followCompany(
	        @PathVariable("companyId") UUID companyId,
	        @RequestHeader("Authorization") String jwt) throws Exception {

	    String email = JwtProvider.getEmailFromJwtToken(jwt);
	    Optional<UserAccount> reqUser = userAccountRepository.findByEmail(email);

	    // Kiểm tra nếu user không tồn tại
	    if (reqUser.isEmpty()) {
	        throw new Exception("Người dùng không tồn tại");
	    }

	    // Gọi service và nhận về kết quả hành động follow/unfollow
	    Map<String, Object> result = companyService.followCompany(companyId, reqUser.get().getUserId());

	    // Trả về phản hồi với message theo kết quả nhận được từ service
	    return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
	}

	
	@GetMapping("/profile-company/{companyId}")
	public ResponseEntity<Company> getCompanyById(@PathVariable("companyId") UUID companyId) throws AllExceptions {
		try {
			Company company = companyService.findCompanyById(companyId);
			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

}

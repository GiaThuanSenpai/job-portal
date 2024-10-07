package com.job_portal.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.job_portal.models.Company;

import com.job_portal.models.UserAccount;
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
	public ResponseEntity<List<Company>> getCompany() {
		List<Company> companies = companyRepository.findAll();
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}

	@PutMapping("/update-company")
	public ResponseEntity<String> updateCompany(@RequestHeader("Authorization") String jwt,
			@RequestBody CompanyDTO company) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		UserAccount user = userAccountRepository.findByEmail(email);

		Optional<Company> reqCompany = companyRepository.findById(user.getUserId());
		if (reqCompany.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			Company newCompany = new Company();
			newCompany.setCompanyName(company.getCompanyName());
			newCompany.setAddress(company.getAddress());
			newCompany.setDescription(company.getDescription());
			newCompany.setLogo(company.getLogo());
			newCompany.setContact(company.getContact());
			newCompany.setEmail(company.getEmail());
			newCompany.setEstablishedTime(company.getEstablished_date());

			boolean isUpdated = companyService.updateCompany(newCompany, reqCompany.get().getCompanyId(),
					company.getIndustryId(), company.getCityId());
			if (isUpdated) {
				return new ResponseEntity<>("Update Company success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("Update Company failed", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Company> followCompany(@PathVariable("companyId") UUID companyId,
			@RequestHeader("Authorization") String jwt) throws Exception {

		String email = JwtProvider.getEmailFromJwtToken(jwt);
		UserAccount reqUser = userAccountRepository.findByEmail(email);

		Company company = companyService.followCompany(companyId, reqUser.getUserId());

		return new ResponseEntity<Company>(company, HttpStatus.ACCEPTED);

	}
}

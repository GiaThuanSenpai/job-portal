package com.job_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.job_portal.models.Industry;
import com.job_portal.repository.IndustryRepository;
import com.job_portal.service.IIndustryService;
import com.social.exceptions.AllExceptions;

@RestController
public class IndustryController {
	
	@Autowired
	private IndustryRepository industryRepository;
	
	@Autowired
	private IIndustryService industryService;
	
	@GetMapping("/industry/get-all")
	public ResponseEntity<List<Industry>> getIndustry() {
		List<Industry> categories = industryRepository.findAll();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
	
	@PostMapping("/industry/create-industry")
	public ResponseEntity<String> createIndustry(@RequestBody Industry industry) {
	    boolean isCreated = industryService.createIndustry(industry);
	    if (isCreated) {
	        return new ResponseEntity<>("Create industry Success", HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>("Create industry Failed", HttpStatus.BAD_REQUEST);
	    }
	}
	
	@GetMapping("/industry/{industry_id}")
	public ResponseEntity<Industry> getIndustryById(@PathVariable("industry_id") Integer industry_id) throws AllExceptions {
		try {
			Industry industry = industryService.findIndustryById(industry_id);
			return new ResponseEntity<>(industry, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}


	@DeleteMapping("/industry/{industry_id}")
	public ResponseEntity<String> deleteIndustry(@PathVariable("industry_id") Integer industry_id) {
		try {
			boolean isDeleted = industryService.deleteIndustry(industry_id);
			if (isDeleted) {
				return new ResponseEntity<>("Industry deleted successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Industry deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/industry/search")
	public List<Industry> searchIndustry(@RequestParam("query") String query) throws AllExceptions {
		List<Industry> industries = industryService.searchIndustry(query);
		return industries;
	}
}

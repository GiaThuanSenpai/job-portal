package com.job_portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job_portal.models.City;
import com.job_portal.repository.CityRepository;

@RestController
public class CityController {
	@Autowired
	CityRepository cityRepository;
	
	@GetMapping("/city/get-all")
	public ResponseEntity<List<City>> getCity() {
		List<City> cities = cityRepository.findAll();
		return new ResponseEntity<>(cities, HttpStatus.OK);
	}
}

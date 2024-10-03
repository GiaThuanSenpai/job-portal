package com.job_portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@GetMapping
	public String homeControllerHandler() {
		return "GiaThuan";
	}

	@GetMapping("/home")
	public String homeControllerHandler2() {
		return "GiaThuan2";
	}
}

package com.job_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.models.BlackListToken;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, String> {	
	 boolean existsByToken(String token);	
}

package com.job_portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.job_portal.models.ForgotPassword;
import com.job_portal.models.UserAccount;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
	
	 Optional<ForgotPassword> findByOtpAndUserAccount(String otp, UserAccount userAccount);
}

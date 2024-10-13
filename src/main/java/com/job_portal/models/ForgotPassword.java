package com.job_portal.models;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "forgot_password")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgotPassword {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fpId;
	
	@Column(nullable = false)
	private String otp;
	
	@Column(nullable = false)
	private Date expirationTime;
	
	@OneToOne
	private UserAccount userAccount;
}

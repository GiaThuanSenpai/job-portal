package com.job_portal.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	public static String generateToken(Authentication auth) {
		// Log the email being put into the JWT
		System.out.println("Generating JWT for email: " + auth.getName());

		String jwt = Jwts.builder().setIssuer("GiaThuanSenpai").setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 86400000))
				.claim("email", auth.getName()) 
				.signWith(key).compact();
		return jwt;
	}

	public static String getEmailFromJwtToken(String jwt) {
		jwt = jwt.substring(7);

		Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

		// Log the email extracted from JWT
		System.out.println("Extracted email from JWT: " + claims.get("email", String.class));

		String email = String.valueOf(claims.get("email"));
		return email;// Ensure this retrieves the correct claim
	}
}
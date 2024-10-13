package com.job_portal.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.job_portal.repository.BlackListTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	@Autowired
	private BlackListTokenRepository blackListTokenRepository;

	public boolean isTokenBlacklisted(String token) {
		return blackListTokenRepository.existsByToken(token);
	}

	private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

	public static String generateToken(Authentication auth) {
		// Log the email being put into the JWT
		System.out.println("Generating JWT for email: " + auth.getName());

		String jwt = Jwts.builder().setIssuer("GiaThuanSenpai").setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 86400000)).claim("email", auth.getName()).signWith(key)
				.compact();
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

	public boolean validateToken(String token) {
		try {
			// Kiểm tra nếu token nằm trong danh sách đen
			if (isTokenBlacklisted(token)) {
				return false;
			}

			// Phân tích và xác thực token
			((JwtParserBuilder) Jwts.builder()).setSigningKey(key).build().parseClaimsJws(token);

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			// Token không hợp lệ
			return false;
		}
	}

	public Date getExpirationDateFromJWT(String token) {
		Claims claims = ((JwtParserBuilder) Jwts.builder()).setSigningKey(key).build().parseClaimsJws(token).getBody();

		return claims.getExpiration();
	}
}
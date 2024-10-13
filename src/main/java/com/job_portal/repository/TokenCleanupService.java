package com.job_portal.repository;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.job_portal.config.JwtProvider;
import com.job_portal.models.BlackListToken;

@Service
public class TokenCleanupService {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Scheduled(cron = "0 0 * * * ?")
    public void cleanUpBlacklistedTokens() {
        List<BlackListToken> tokens = blackListTokenRepository.findAll();
        for (BlackListToken token : tokens) {
        	 Date expirationDate =  jwtProvider.getExpirationDateFromJWT(token.getToken());
             LocalDateTime expiration = convertDateToLocalDateTime(expirationDate);
            if (expiration.isBefore(LocalDateTime.now())) {
                blackListTokenRepository.delete(token);
            }
        }
    }

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }
}

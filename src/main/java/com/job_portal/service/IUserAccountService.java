package com.job_portal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.job_portal.DTO.DailyAccountCount;
import com.job_portal.models.UserAccount;
import com.social.exceptions.AllExceptions;

public interface IUserAccountService {

	public boolean deleteUser(UUID userId) throws AllExceptions;

	public boolean updateUser(UserAccount user, UUID userId) throws AllExceptions;

	public List<UserAccount> searchUser(String query);
	public UserAccount findUserByEmail(String email);
	public UserAccount findUserById(UUID userId) throws AllExceptions;
	public UserAccount findUserByJwt(String jwt);
	public List<DailyAccountCount> getDailyAccountCounts(LocalDateTime startDate, LocalDateTime endDate);
}

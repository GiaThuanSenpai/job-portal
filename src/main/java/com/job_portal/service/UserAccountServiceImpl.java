package com.job_portal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.DailyAccountCount;
import com.job_portal.config.JwtProvider;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.UserAccountRepository;
import com.social.exceptions.AllExceptions;

@Service
public class UserAccountServiceImpl implements IUserAccountService {

	@Autowired
	UserAccountRepository userAccountRepository;

	@Override
	public UserAccount findUserByJwt(String jwt) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);
		return user.get();
	}


	@Override
	public UserAccount findUserByEmail(String email) {
		Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
		return userAccount.get();
	}

	@Override
	public boolean deleteUser(UUID userId) throws AllExceptions{
		Optional<UserAccount> user = userAccountRepository.findById(userId);

		if (user.isEmpty()) {
			throw new AllExceptions("User not exist with id: " + userId);
		}

		userAccountRepository.delete(user.get());
		return true;
	}

	@Override
	public boolean updateUser(UserAccount user, UUID userId) throws AllExceptions {
		Optional<UserAccount> newUser = userAccountRepository.findById(userId);
		if (newUser.isEmpty()) {
			throw new AllExceptions("User not exist with id " + userId);
		}
		UserAccount oldUser = newUser.get();

		boolean isUpdated = false;

		if (user.getUserName() != null) {
			oldUser.setUserName(user.getUserName());
			isUpdated = true;
		}
		if (user.getAvatar() != null) {
			oldUser.setAvatar(user.getAvatar());
			isUpdated = true;
		}
		if(user.getEmail() != null) {
			oldUser.setEmail(user.getEmail());
			isUpdated = true;
		}
		if (user.getPassword() != null) {
			oldUser.setPassword(user.getPassword());
			isUpdated = true;
		}
		
		if (isUpdated) {
			userAccountRepository.save(oldUser);
		}

		return isUpdated;
	}

	@Override
	public List<UserAccount> searchUser(String query) {
		return userAccountRepository.searchUser(query);
	}

	@Override
	public UserAccount findUserById(UUID userId) throws AllExceptions{
		Optional<UserAccount> user = userAccountRepository.findById(userId);
		if (user.isPresent()) {
			return user.get();
		}
		throw new AllExceptions("User not exist with user_id " + userId);
		
	}


	@Override
	public List<DailyAccountCount> getDailyAccountCounts(LocalDateTime startDate, LocalDateTime endDate) {
        List<Object[]> results = userAccountRepository.countNewAccountsPerDay(startDate, endDate);
        List<DailyAccountCount> dailyAccountCounts = new ArrayList<>();

        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            Long count = ((Number) result[1]).longValue();
            dailyAccountCounts.add(new DailyAccountCount(date, count));
        }

        return dailyAccountCounts;
    }



}

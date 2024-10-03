package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		UserAccount user = userAccountRepository.findByEmail(email);
		return user;
	}


	@Override
	public UserAccount findUserByEmail(String email) {
		UserAccount userAccount = userAccountRepository.findByEmail(email);
		return userAccount;
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

}

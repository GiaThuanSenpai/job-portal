package com.job_portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.job_portal.models.UserAccount;
import com.job_portal.repository.UserAccountRepository;


@Service
public class AccountDetailServiceImpl implements UserDetailsService {


	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserAccount> user = userAccountRepository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with email: " + username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
	}
	
	

}

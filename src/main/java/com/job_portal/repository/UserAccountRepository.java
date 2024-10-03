package com.job_portal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>{
	public UserAccount findByEmail(String email);
	public Optional<UserAccount> findById(UUID userId );

	@Query("SELECT u FROM UserAccount u WHERE u.userName LIKE %:query% OR u.email LIKE %:query%")
	public List<UserAccount> searchUser(@Param("query") String query);
}

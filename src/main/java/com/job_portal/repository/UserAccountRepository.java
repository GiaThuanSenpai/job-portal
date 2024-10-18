package com.job_portal.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.UserAccount;

import jakarta.transaction.Transactional;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
	public Optional<UserAccount> findByEmail(String email);

	public Optional<UserAccount> findById(UUID userId);

	@Query("SELECT u FROM UserAccount u WHERE u.userName LIKE %:query% OR u.email LIKE %:query%")
	public List<UserAccount> searchUser(@Param("query") String query);

	@Transactional
	@Modifying
	@Query("UPDATE UserAccount u SET u.password = :password WHERE u.email = :email")
	void updatePassword(@Param("email") String email, @Param("password") String password);

	@Query(value = "SELECT DATE(u.create_date) AS date, COUNT(u.user_id) AS count " + "FROM user_account u "
			+ "WHERE u.create_date BETWEEN :startDate AND :endDate " + "GROUP BY DATE(u.create_date) "
			+ "ORDER BY DATE(u.create_date)", nativeQuery = true)
	List<Object[]> countNewAccountsPerDay(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);
}

package com.job_portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.models.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
	Optional<UserType> findById(Integer id);
}

package com.job_portal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Company;
import com.job_portal.models.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Integer>{
	@Query("SELECT e FROM Experience e WHERE e.seeker.userId = :userId")
	List<Experience> findExpByUserId(@Param("userId") UUID userId);
}

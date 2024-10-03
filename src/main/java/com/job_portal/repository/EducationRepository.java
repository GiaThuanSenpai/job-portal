package com.job_portal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Education;

public interface EducationRepository extends JpaRepository<Education, Integer> {
	@Query("SELECT e FROM Education e WHERE e.seeker.userId = :userId")
	List<Education> findEduByUserId(@Param("userId") UUID userId);
}

package com.job_portal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.CV;

public interface CVRepository extends JpaRepository<CV, Integer>{
	@Query("SELECT c FROM CV c WHERE c.seeker.userId = :userId")
	List<CV> findCVBySeekerId(@Param("userId") UUID userId);
}

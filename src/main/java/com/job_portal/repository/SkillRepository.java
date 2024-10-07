package com.job_portal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Review;
import com.job_portal.models.Skills;

public interface SkillRepository extends JpaRepository<Skills, Integer>{
	@Query("SELECT r FROM Review r WHERE r.company.companyId = :companyId")
	public List<Skills> findReviewByCompanyId(@Param("companyId") UUID companyId);
}

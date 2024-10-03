package com.job_portal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Company;
import com.job_portal.models.Seeker;


public interface SeekerRepository extends JpaRepository<Seeker, UUID> {

	public Optional<Seeker> findById(UUID userId);

	@Query("SELECT s FROM Seeker s WHERE s.userAccount.userName LIKE %:userName%")
	public List<Seeker> findSeekerByUserName(@Param("userName") String userName);
	
	@Query("SELECT s FROM Seeker s WHERE s.industry.industryName LIKE %:industryName%")
	public List<Seeker> findSeekerByIndustryName(@Param("industryName") String industryName);
}

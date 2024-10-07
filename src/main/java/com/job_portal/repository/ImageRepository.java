package com.job_portal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Experience;
import com.job_portal.models.ImageCompany;

public interface ImageRepository extends JpaRepository<ImageCompany, Integer>{
	@Query("SELECT i FROM ImageCompany i WHERE i.company.companyId = :companyId")
	List<ImageCompany> findImgByCompanyId(@Param("companyId") UUID companyId);
}

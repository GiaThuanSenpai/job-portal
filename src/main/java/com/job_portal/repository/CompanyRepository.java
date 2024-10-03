package com.job_portal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.Company;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
	
	@Query("SELECT c FROM Company c WHERE c.companyName LIKE %:companyName%")
    public List<Company> findCompanyByCompanyName(@Param("companyName") String companyName);
	
	@Query("SELECT c FROM Company c WHERE c.companyId = :companyId")
	public Optional<Company> findCompanyByCompanyId(@Param("companyId") UUID companyId);

	@Query("SELECT c FROM Company c WHERE c.city.cityName LIKE %:cityName%")
	public List<Company> findCompaniesByCityName(@Param("cityName") String cityName);
	
	@Query("SELECT c FROM Company c WHERE c.industry.industryName LIKE %:industryName%")
	public List<Company> findCompaniesByIndustryName(@Param("industryName") String industryName);
	
}

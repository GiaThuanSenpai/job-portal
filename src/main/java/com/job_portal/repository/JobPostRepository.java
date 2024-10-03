package com.job_portal.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost, UUID> {

	 @Query("SELECT j FROM JobPost j WHERE (j.title LIKE %:query% OR j.typeOfWork LIKE %:query%) AND j.isApprove = true")
	 public List<JobPost> findJobByJobName(@Param("query") String query);

	@Query("SELECT j FROM JobPost j WHERE j.experience LIKE %:experience% AND j.isApprove = true")
	public List<JobPost> findJobByExperience(@Param("experience") String experience);

	@Query("SELECT j FROM JobPost j WHERE j.city.id = :cityId AND j.isApprove = true")
	public List<JobPost> findJobByCityId(@Param("cityId") Integer cityId);

	 // Lọc các JobPost có salary >= minSalary và đã phê duyệt
    public List<JobPost> findBySalaryGreaterThanEqualAndIsApproveTrue(Long minSalary);

    // Lọc các JobPost có salary <= maxSalary và đã phê duyệt
    public List<JobPost> findBySalaryLessThanEqualAndIsApproveTrue(Long maxSalary);

    // Lọc các JobPost có salary giữa minSalary và maxSalary và đã phê duyệt
    public List<JobPost> findBySalaryBetweenAndIsApproveTrue(Long minSalary, Long maxSalary);
    
    
}
package com.job_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job_portal.models.ApplyJob;
import com.job_portal.models.IdApplyJob;

@Repository
public interface ApplyJobRepository extends JpaRepository<ApplyJob, IdApplyJob> {

}

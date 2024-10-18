package com.job_portal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.job_portal.models.ApplyJob;
import com.job_portal.models.IdApplyJob;

@Repository
public interface ApplyJobRepository extends JpaRepository<ApplyJob, IdApplyJob> {
	Optional<ApplyJob> findByPostIdAndUserId(UUID postId, UUID userId);
}

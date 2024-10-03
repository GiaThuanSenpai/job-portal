package com.job_portal.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.models.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

}

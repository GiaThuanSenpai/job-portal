package com.job_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.models.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {

}

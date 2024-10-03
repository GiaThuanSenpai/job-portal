package com.job_portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.job_portal.models.City;

public interface CityRepository extends JpaRepository<City, Integer> {

	@Query("SELECT c FROM City c WHERE c.cityName LIKE %:query%")
	public List<City> searchCity(@Param("query") String query);	
	
	City findByCityName(String cityName);
}

package com.job_portal.service;

import java.util.List;

import com.job_portal.models.Industry;
import com.social.exceptions.AllExceptions;

public interface IIndustryService {
	public boolean createIndustry(Industry industry);
	public boolean deleteIndustry(Integer industryId) throws AllExceptions;
	public boolean updateIndustry(Industry industry, Integer industryId) throws AllExceptions;
	public List<Industry> searchIndustry(String query) throws AllExceptions;
	public Industry findIndustryById(Integer industryId) throws AllExceptions;
}

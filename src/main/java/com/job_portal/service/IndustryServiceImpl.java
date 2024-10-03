package com.job_portal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.Industry;
import com.job_portal.models.UserAccount;
import com.job_portal.repository.IndustryRepository;
import com.social.exceptions.AllExceptions;

@Service
public class IndustryServiceImpl implements IIndustryService {

	@Autowired
	IndustryRepository industryRepository;
	

	@Override
	public boolean createIndustry(Industry industry) {
		Industry newIndustry = new Industry();
		newIndustry.setIndustryId(industry.getIndustryId());
		newIndustry.setIndustryName(industry.getIndustryName());

		try {
			Industry savedIndustry= industryRepository.save(newIndustry);
			return savedIndustry != null;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteIndustry(Integer industry_id) throws AllExceptions {
		Optional<Industry> industry = industryRepository.findById(industry_id);

		if (industry.isEmpty()) {
			throw new AllExceptions("Industry not exist with id: " + industry_id);
		}

		industryRepository.delete(industry.get());
		return true;
	}

	@Override
	public boolean updateIndustry(Industry industry, Integer industry_id) throws AllExceptions {
		Optional<Industry> newIndustry = industryRepository.findById(industry_id);
		if (newIndustry.isEmpty()) {
			throw new AllExceptions("Industry not exist with id " + industry_id);
		}
		Industry oldIndustry = newIndustry.get();

		boolean isUpdated = false;

		if (industry.getIndustryName() != null) {
			oldIndustry.setIndustryName(industry.getIndustryName());
			isUpdated = true;
		}
		if (isUpdated) {
			industryRepository.save(oldIndustry);
		}

		return isUpdated;
	}

	@Override
	public List<Industry> searchIndustry(String query) throws AllExceptions {
		return industryRepository.searchIndustry(query);
	}

	@Override
	public Industry findIndustryById(Integer industry_id) throws AllExceptions {
		Optional<Industry> industry = industryRepository.findById(industry_id);
		if (industry.isPresent()) {
			return industry.get();
		}
		throw new AllExceptions("Industry not exist with id: " + industry_id);
	}

	
}

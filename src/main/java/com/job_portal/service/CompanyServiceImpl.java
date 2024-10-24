package com.job_portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.Industry;
import com.job_portal.models.Seeker;
import com.job_portal.repository.CityRepository;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.IndustryRepository;
import com.social.exceptions.AllExceptions;

@Service
public class CompanyServiceImpl implements ICompanyService {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	IIndustryService industryService;
	@Autowired
	CityRepository cityRepository;
	@Autowired
	IndustryRepository industryRepository;
	@Autowired
	ISeekerService seekerService;

	@Override
	public boolean deleteCompany(UUID companyId) throws AllExceptions {
		Optional<Company> company = companyRepository.findById(companyId);

		if (company.isEmpty()) {
			throw new AllExceptions("Company not exist with id: " + companyId);
		}

		companyRepository.delete(company.get());
		return true;
	}

	@Override
	public boolean updateCompany(Company company, UUID companyId, Integer industryId, Integer cityId)
			throws AllExceptions {
		// Tìm kiếm Company theo id
		Optional<Company> existingCompany = companyRepository.findById(companyId);

		// Lấy đối tượng Company cũ
		Company oldCompany = existingCompany.get();
		boolean isUpdated = false;

		// Cập nhật các trường cơ bản
		if (company.getCompanyName() != null) {
			oldCompany.setCompanyName(company.getCompanyName());
			isUpdated = true;
		}

		if (company.getAddress() != null) {
			oldCompany.setAddress(company.getAddress());
			isUpdated = true;
		}

		if (company.getDescription() != null) {
			oldCompany.setDescription(company.getDescription());
			isUpdated = true;
		}

		if (company.getLogo() != null) {
			oldCompany.setLogo(company.getLogo());
			isUpdated = true;
		}

		if (company.getContact() != null) {
			oldCompany.setContact(company.getContact());
			isUpdated = true;
		}

		if (company.getEmail() != null) {
			oldCompany.setEmail(company.getEmail());
			isUpdated = true;
		}
		
		if (company.getEstablishedTime() != null) {
			oldCompany.setEstablishedTime(company.getEstablishedTime());
			isUpdated = true;
		}

		// Tìm Industry mới dựa trên industryId
		if (industryId != null) {
			Optional<Industry> newIndustry = industryRepository.findById(industryId);
	
			// Cập nhật Industry nếu khác
			if (!newIndustry.get().equals(oldCompany.getIndustry())) {
				oldCompany.setIndustry(newIndustry.get());
				isUpdated = true;
			}
		}

		// Tìm City mới dựa trên cityId
		if (cityId != null) {
			Optional<City> newCity = cityRepository.findById(cityId);
	
			// Cập nhật City nếu khác
			if (!newCity.get().equals(oldCompany.getCity())) {
				oldCompany.setCity(newCity.get());
				isUpdated = true;
			}
		}

		// Nếu có thay đổi, lưu lại đối tượng
		if (isUpdated) {
			companyRepository.save(oldCompany);
		}

		return isUpdated; // Trả về true nếu có cập nhật, false nếu không
	}

	@Override
	public List<Company> searchCompaniesByName(String companyName) throws AllExceptions {
		try {

			List<Company> companies = companyRepository.findCompanyByCompanyName(companyName);
			if (companies.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công ty nào");
			}

			return companies;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<Company> searchCompaniesByCity(String cityName) throws AllExceptions {
		try {

			List<Company> companies = companyRepository.findCompaniesByCityName(cityName);
			if (companies.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công ty nào với tên thành phố: " + cityName);
			}
			return companies;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public Company findCompanyById(UUID companyId) throws AllExceptions {
		try {
			// Tìm kiếm công ty dựa trên companyId
			Optional<Company> companyOptional = companyRepository.findCompanyByCompanyId(companyId);

			// Nếu không tìm thấy công ty, ném ra ngoại lệ
			if (!companyOptional.isPresent()) {
				throw new AllExceptions("Không tìm thấy công ty nào");
			}

			// Trả về công ty nếu tìm thấy
			return companyOptional.get();
		} catch (Exception e) {
			// Ném ra ngoại lệ nếu có lỗi xảy ra
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<Company> searchCompaniesByIndustry(String industryName) throws AllExceptions {
		try {

			List<Company> companies = companyRepository.findCompaniesByIndustryName(industryName);
			if (companies.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công ty nào với tên ngành: " + industryName);
			}
			return companies;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> followCompany(UUID companyId, UUID userId) throws AllExceptions {
	    
	    Company company = findCompanyById(companyId);
	    Seeker seeker = seekerService.findSeekerById(userId);

	    Map<String, Object> result = new HashMap<>();
	    
	    if (company.getFollows().contains(seeker)) {
	        company.getFollows().remove(seeker);
	        result.put("action", "unfollow");
	        result.put("message", "Bỏ theo dõi công ty thành công");
	    } else {
	        company.getFollows().add(seeker);
	        result.put("action", "follow");
	        result.put("message", "Theo dõi công ty thành công");
	    }

	    companyRepository.save(company);
	    return result;
	}


}

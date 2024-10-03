package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.JobPostDTO;
import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.Industry;
import com.job_portal.models.JobPost;
import com.job_portal.models.Seeker;
import com.job_portal.repository.CityRepository;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.JobPostRepository;
import com.social.exceptions.AllExceptions;

@Service
public class JobPostServiceImpl implements IJobPostService {

	@Autowired
	private JobPostRepository jobPostRepository;
	@Autowired
	CityRepository cityRepository;
	@Autowired
	CompanyRepository companyRepository;

	@Override
	public boolean createJob(JobPostDTO jobPostDTO, UUID companyId) {
		City city = cityRepository.findById(jobPostDTO.getCityId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid City ID"));

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Company ID"));

		// Build the JobPost entity
		JobPost jobPost = new JobPost();
		jobPost.setCreateDate(jobPostDTO.getCreateDate());
		jobPost.setExpireDate(jobPostDTO.getExpireDate());
		jobPost.setTitle(jobPostDTO.getTitle());
		jobPost.setDescription(jobPostDTO.getDescription());
		jobPost.setBenefit(jobPostDTO.getBenefit());
		jobPost.setExperience(jobPostDTO.getExperience());
		jobPost.setSalary(jobPostDTO.getSalary());
		jobPost.setRequirement(jobPostDTO.getRequirement());
		jobPost.setLocation(jobPostDTO.getLocation());
		jobPost.setTypeOfWork(jobPostDTO.getTypeOfWork());
		jobPost.setPosition(jobPostDTO.getPosition());
		jobPost.setStatus(jobPostDTO.getStatus());
		jobPost.setCompany(company);
		jobPost.setCity(city);
		jobPost.setApprove(false);
		jobPost.setNiceToHaves(jobPostDTO.getNiceToHaves());
		// Save the JobPost entity
		try {
			JobPost saveJobPost = jobPostRepository.save(jobPost);
			return saveJobPost != null;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteJob(UUID postId) throws AllExceptions {
		Optional<JobPost> jobPost = jobPostRepository.findById(postId);

		if (jobPost.isEmpty()) {
			throw new AllExceptions("Seeker not exist with id: " + postId);
		}

		jobPostRepository.delete(jobPost.get());
		return true;
	}

	@Override
	public boolean updateJob(JobPost jobPost, UUID postId, UUID companyId, Integer cityId) throws AllExceptions {
		// Tìm kiếm Company theo id
		Optional<JobPost> existingJob = jobPostRepository.findById(postId);

		if (existingJob.isEmpty()) {
			throw new AllExceptions("Job not exist with id " + postId);
		}

		// Lấy đối tượng Company cũ
		JobPost oldJob = existingJob.get();
		boolean isUpdated = false;

		// Cập nhật các trường cơ bản
		if (jobPost.getCreateDate() != null) {
			oldJob.setCreateDate(jobPost.getCreateDate());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (jobPost.getExpireDate() != null) {
			oldJob.setExpireDate(jobPost.getExpireDate());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (jobPost.getTitle() != null) {
			oldJob.setTitle(jobPost.getTitle());
			isUpdated = true;
		}
		// Cập nhật các trường cơ bản
		if (jobPost.getDescription() != null) {
			oldJob.setDescription(jobPost.getDescription());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (jobPost.getBenefit() != null) {
			oldJob.setBenefit(jobPost.getBenefit());
			isUpdated = true;
		}

		// Cập nhật các trường cơ bản
		if (jobPost.getExperience() != null) {
			oldJob.setExperience(jobPost.getExperience());
			isUpdated = true;
		}

		if (jobPost.getSalary() != null) {
			oldJob.setSalary(jobPost.getSalary());
			isUpdated = true;
		}

		if (jobPost.getRequirement() != null) {
			oldJob.setRequirement(jobPost.getRequirement());
			isUpdated = true;
		}
		if (jobPost.getLocation() != null) {
			oldJob.setLocation(jobPost.getLocation());
			isUpdated = true;
		}
		if (jobPost.getTypeOfWork() != null) {
			oldJob.setTypeOfWork(jobPost.getTypeOfWork());
			isUpdated = true;
		}
		if (jobPost.getPosition() != null) {
			oldJob.setPosition(jobPost.getPosition());
			isUpdated = true;
		}

		if (jobPost.getStatus() != null) {
			oldJob.setStatus(jobPost.getStatus());
			isUpdated = true;
		}
		
		if (jobPost.getNiceToHaves() != null) {
			oldJob.setNiceToHaves(jobPost.getNiceToHaves());
			isUpdated = true;
		}

		// Tìm Industry mới dựa trên industryId
		if (companyId != null) {
			Optional<Company> newCompany = companyRepository.findById(companyId);
			if (newCompany.isEmpty()) {
				throw new AllExceptions("Company not exist with id " + companyId);
			}
			// Cập nhật Industry nếu khác
			if (!newCompany.get().equals(oldJob.getCompany())) {
				oldJob.setCompany(newCompany.get());
				isUpdated = true;
			}
		}

		// Tìm Industry mới dựa trên industryId
		if (cityId != null) {
			Optional<City> newCity = cityRepository.findById(cityId);
			if (newCity.isEmpty()) {
				throw new AllExceptions("City not exist with id " + cityId);
			}
			// Cập nhật Industry nếu khác
			if (!newCity.get().equals(oldJob.getCity())) {
				oldJob.setCity(newCity.get());
				isUpdated = true;
			}
		}

		if (isUpdated) {
			jobPostRepository.save(oldJob);
		}

		return isUpdated;
	}

	@Override
	public List<JobPost> searchJobByJobName(String title) throws AllExceptions {
		try {
			List<JobPost> jobs = jobPostRepository.findJobByJobName(title);
			if (jobs.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công việc nào với tên: " + title);
			}

			return jobs;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<JobPost> searchJobByExperience(String experience) throws AllExceptions {
		try {

			List<JobPost> jobs = jobPostRepository.findJobByExperience(experience);
			if (jobs.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công viêc nào với kinhg nghiệm: " + experience);
			}

			return jobs;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<JobPost> searchJobByCity(Integer cityId) throws AllExceptions {
		try {

			List<JobPost> jobs = jobPostRepository.findJobByCityId(cityId);
			if (jobs.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công viêc nào ở: " + cityId);
			}

			return jobs;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

	@Override
	public List<JobPost> findBySalaryGreaterThanEqual(Long minSalary) throws AllExceptions {
		try {
			List<JobPost> jobPosts = jobPostRepository.findBySalaryGreaterThanEqualAndIsApproveTrue(minSalary);
			if (jobPosts.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công việc với lương >= " + minSalary);
			}
			return jobPosts;
		} catch (AllExceptions e) {
			throw e; // Ném lại ngoại lệ đã định nghĩa
		} catch (Exception e) {
			throw new AllExceptions("Lỗi khi tìm kiếm công việc với lương >= " + minSalary);
		}
	}

	@Override
	public List<JobPost> findBySalaryLessThanEqual(Long maxSalary) throws AllExceptions {
		try {
			List<JobPost> jobPosts = jobPostRepository.findBySalaryLessThanEqualAndIsApproveTrue(maxSalary);
			if (jobPosts.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công việc với lương >= " + maxSalary);
			}
			return jobPosts;
		} catch (AllExceptions e) {
			throw e; // Ném lại ngoại lệ đã định nghĩa
		} catch (Exception e) {
			throw new AllExceptions("Lỗi khi tìm kiếm công việc với lương < " + maxSalary);
		}
	}

	@Override
	public List<JobPost> findBySalaryBetween(Long minSalary, Long maxSalary) throws AllExceptions {
		if (minSalary == null || maxSalary == null) {
			throw new AllExceptions("minSalary và maxSalary không được để trống");
		}
		if (minSalary > maxSalary) {
			throw new AllExceptions("minSalary không thể lớn hơn maxSalary");
		}
		try {
			List<JobPost> jobPosts = jobPostRepository.findBySalaryBetweenAndIsApproveTrue(minSalary, maxSalary);
			if (jobPosts.isEmpty()) {
				throw new AllExceptions("Không tìm thấy công việc với lương >= " + minSalary + " và < " + maxSalary);
			}
			return jobPosts;
		} catch (AllExceptions e) {
			throw e; // Ném lại ngoại lệ đã định nghĩa
		} catch (Exception e) {
			throw new AllExceptions("Lỗi khi tìm kiếm công việc với lương >= " + minSalary + " và < " + maxSalary);
		}

	}

	@Override
	public boolean approveJob(UUID postId) {
		Optional<JobPost> jobPostOpt = jobPostRepository.findById(postId);
		if (jobPostOpt.isPresent()) {
			JobPost jobPost = jobPostOpt.get();
			jobPost.setApprove(true); // Đặt trường isApprove thành true
			jobPostRepository.save(jobPost); // Lưu công việc đã cập nhật
			return true;
		}
		return false; // Trả về false nếu không tìm thấy công việc
	}

}

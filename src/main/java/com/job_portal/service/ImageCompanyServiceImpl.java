package com.job_portal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.DTO.ImageDTO;
import com.job_portal.models.City;
import com.job_portal.models.Company;
import com.job_portal.models.ImageCompany;
import com.job_portal.models.JobPost;
import com.job_portal.models.Skills;
import com.job_portal.repository.CompanyRepository;
import com.job_portal.repository.ImageRepository;
import com.social.exceptions.AllExceptions;

@Service
public class ImageCompanyServiceImpl implements IImageCompanyService {

	@Autowired
	CompanyRepository companyRepository;
	@Autowired
	ImageRepository imageRepository;

	@Override
	public boolean createImg(ImageDTO imageDTO, UUID companyId) {
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Company ID"));

		// Build the JobPost entity
		ImageCompany imageCompany = new ImageCompany();

		imageCompany.setCompany(company);
		imageCompany.setPathImg(imageDTO.getPathImg());

		try {
			ImageCompany saveImg = imageRepository.save(imageCompany);
			return saveImg != null;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteImg(Integer imgId) throws AllExceptions {
		Optional<ImageCompany> image = imageRepository.findById(imgId);

		if (image.isEmpty()) {
			throw new AllExceptions("Image not exist");
		}

		imageRepository.delete(image.get());
		return true;
	}

	@Override
	public boolean updateImg(ImageDTO imageDTO, Integer imgId) throws AllExceptions {
		Optional<ImageCompany> existingImg = imageRepository.findById(imgId);

		if (existingImg.isEmpty()) {
			throw new AllExceptions("Imgage not exist with id " + imgId);
		}


		ImageCompany oldImg = existingImg.get();
		boolean isUpdated = false;

		if (imageDTO.getPathImg() != null) {
			oldImg.setPathImg(imageDTO.getPathImg());
			isUpdated = true;
		}

		if (isUpdated) {
			imageRepository.save(oldImg);
		}

		return isUpdated;
	}

	@Override
	public List<ImageCompany> findImgByCompanyId(UUID companyId) throws AllExceptions {
		try {

			List<ImageCompany> imgs = imageRepository.findImgByCompanyId(companyId);
			if (imgs.isEmpty()) {
				throw new AllExceptions("Không tìm thấy hình ảnh");
			}

			return imgs;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

}

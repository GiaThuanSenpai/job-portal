package com.job_portal.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.job_portal.DTO.CVDTO;
import com.job_portal.models.CV;
import com.job_portal.models.Seeker;
import com.job_portal.repository.CVRepository;
import com.job_portal.repository.SeekerRepository;
import com.social.exceptions.AllExceptions;

public class CVServiceImpl implements ICVService {

	@Autowired
	CVRepository cvRepository;
	@Autowired
	SeekerRepository seekerRepository;
	@Override
	public boolean createCV(CVDTO cvdto, UUID userId) {
		Seeker seeker = seekerRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Seeker ID"));

		// Build the JobPost entity
		CV cv = new CV();

		cv.setSeeker(seeker);
		cv.setPathCV(cvdto.getPathCV());
		cv.setIsMain(false);

		try {
			CV saveCV = cvRepository.save(cv);
			return saveCV != null;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean deleteCV(Integer cvId) throws AllExceptions {
		Optional<CV> cv = cvRepository.findById(cvId);

		if (cv.isEmpty()) {
			throw new AllExceptions("Image not exist");
		}

		cvRepository.delete(cv.get());
		return true;
	}

	@Override
	public boolean updateImg(CVDTO cvdto, Integer cvId) throws AllExceptions {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CV> findCVBySeekerId(UUID userId) throws AllExceptions {
		try {

			List<CV> cvs = cvRepository.findCVBySeekerId(userId);
			if (cvs.isEmpty()) {
				throw new AllExceptions("Không tìm thấy CV");
			}

			return cvs;
		} catch (Exception e) {
			throw new AllExceptions(e.getMessage());
		}
	}

}

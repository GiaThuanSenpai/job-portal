package com.job_portal.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.job_portal.DTO.ImageDTO;

import com.job_portal.config.JwtProvider;
import com.job_portal.models.ImageCompany;

import com.job_portal.models.UserAccount;

import com.job_portal.repository.ImageRepository;

import com.job_portal.repository.UserAccountRepository;
import com.job_portal.service.IImageCompanyService;

import com.social.exceptions.AllExceptions;

@RestController
@RequestMapping("/image-company")
public class ImageCompanyController {
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	IImageCompanyService imageCompanyService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@GetMapping("/get-all")
	public ResponseEntity<List<ImageCompany>> getImage() {
		List<ImageCompany> imgs = imageRepository.findAll();
		return new ResponseEntity<>(imgs, HttpStatus.OK);
	}

	@PostMapping("/create-image")
	public ResponseEntity<String> createImage(@RequestHeader("Authorization") String jwt,
			@RequestBody ImageDTO imageDTO) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);

		boolean isCreated = imageCompanyService.createImg(imageDTO, user.get().getUserId());
		if (isCreated) {
			return new ResponseEntity<>("Thêm hình ảnh thành công", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Thêm hình ảnh thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@DeleteMapping("/delete-image/{imgId}")
	public ResponseEntity<String> deleteImage(@PathVariable("imgId") Integer imgId) {
		try {
			boolean isDeleted = imageCompanyService.deleteImg(imgId);
			if (isDeleted) {
				return new ResponseEntity<>("Xóa hình ảnh thành công", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Xóa hình ảnh thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/searchImage")
	public ResponseEntity<Object> searchImage(@RequestHeader("Authorization") String jwt) {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		Optional<UserAccount> user = userAccountRepository.findByEmail(email);
		try {
			List<ImageCompany> imgs = imageCompanyService.findImgByCompanyId(user.get().getUserId());
			return ResponseEntity.ok(imgs);
		} catch (AllExceptions e) {
			// Trả về thông báo từ service
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			// Trả về thông báo lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Đã xảy ra lỗi trong quá trình xử lý yêu cầu.");
		}
	}
}

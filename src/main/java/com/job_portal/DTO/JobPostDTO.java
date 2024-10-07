package com.job_portal.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostDTO {
	private LocalDateTime createDate;
	private LocalDateTime expireDate;
	private String title;
	private String description;
	private String benefit;
	private String experience;
	private Long salary;
	private String requirement;
	private String location;
	private String typeOfWork;
	private String position;
	private String status;
	private Integer cityId;
	private UUID companyId;
	private boolean isApprove;
	private String niceToHaves;
	private List<Integer> skillIds; // Danh sách ID của Skills
}

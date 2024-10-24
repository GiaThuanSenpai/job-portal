package com.job_portal.DTO;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationDTO {
	private UUID postId;
	private String title;
	private String description;
	private String experience;
	private Long salary;
	private String location;
	private String typeOfWork;
}

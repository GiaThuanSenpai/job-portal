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
public class ImageDTO {
	private UUID companyId;
	private String pathImg;

}

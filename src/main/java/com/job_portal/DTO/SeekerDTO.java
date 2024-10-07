package com.job_portal.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeekerDTO {
	private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String description;
    private String emailContact;
    private Integer industryId; 
    private List<Integer> skillIds; // Danh sách ID của Skills
    private List<SocialLinkDTO> socialLinks = new ArrayList<>();
}

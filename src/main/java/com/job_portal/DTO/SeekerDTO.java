package com.job_portal.DTO;

import java.time.LocalDate;
import java.util.UUID;

import com.job_portal.models.Industry;
import com.job_portal.models.UserAccount;

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
}

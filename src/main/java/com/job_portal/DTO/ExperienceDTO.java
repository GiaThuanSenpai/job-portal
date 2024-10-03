package com.job_portal.DTO;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {

    private Integer experienceId;
    private UUID userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrentJob;
    private String jobTitle;
    private String companyName;
    private String description;

}
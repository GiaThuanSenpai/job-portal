package com.job_portal.DTO;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EducationDTO {

    private Integer educationId;

    private String certificateDegreeName;

    private String major;

    private String universityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String gpa;

    private UUID userId;
}

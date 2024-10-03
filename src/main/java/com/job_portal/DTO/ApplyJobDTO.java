package com.job_portal.DTO;

import java.time.LocalDateTime;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyJobDTO {
    private UUID postId;

    private String pathCV;

    private LocalDateTime applyDate;

    private boolean isSave;
    private String fullName;
    private String email;
    private String description;
}


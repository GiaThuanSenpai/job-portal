package com.job_portal.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Integer educationId;

    @Column(name = "certificate_degree_name", nullable = false)
    private String certificateDegreeName;

    @Column(name = "major", length = 100, nullable = false)
    private String major;

    @Column(name = "university_name", length = 100, nullable = false)
    private String universityName;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "gpa", length = 200)
    private String gpa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Seeker seeker;
}
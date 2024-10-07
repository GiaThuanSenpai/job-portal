package com.job_portal.models;

import lombok.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "post_id")
    private UUID postId;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @Lob
    @Column(name = "benefit", columnDefinition = "MEDIUMTEXT")
    private String benefit;

    @Lob
    @Column(name = "experience", columnDefinition = "MEDIUMTEXT")
    private String experience;

    @Column(name = "salary", nullable = false)
    private Long salary;

    @Lob
    @Column(name = "requirement", columnDefinition = "MEDIUMTEXT")
    private String requirement;

    @Column(name = "location", length = 200, nullable = false)
    private String location;

    @Column(name = "type_of_work", length = 50, nullable = false)
    private String typeOfWork;

    @Column(name = "position", length = 50, nullable = false)
    private String position;

    @Column(name = "status", length = 50, nullable = false)
    private String status;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id")
	private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "is_approve", columnDefinition = "BIT(1)")
    private boolean isApprove;
    
    @Lob
    @Column(name = "nice_to_haves", columnDefinition = "MEDIUMTEXT")
    private String niceToHaves;
    
    @ManyToMany
	private List<Skills> skills = new ArrayList<>();

    // Optional: Add auditing fields like createdBy, updatedBy, etc.
}
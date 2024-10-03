package com.job_portal.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "review_id")
    private UUID reviewId;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Lob
    @Column(name = "message", columnDefinition = "MEDIUMTEXT")
    private String message;

    @Column(name = "star", nullable = false)
    private Integer star;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Seeker seeker;
    
    @JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
	private Company company;
}

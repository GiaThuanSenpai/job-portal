package com.job_portal.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {
	@Id
	@Column(name = "company_id")
	private UUID companyId;

	@Column(name = "company_name", length = 100)
	private String companyName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "industry_id")
	private Industry industry;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id")
	private City city;

	@Column(name = "address", length = 200)
	private String address;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "logo", length = 200)
	private String logo;

	@Column(name = "contact", length = 15)
	private String contact;

	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "established_time", length = 50)
	private LocalDate establishedTime;
	
	@JsonIgnore
	@OneToMany
	private List<Review> reviews = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany
	private List<Seeker> follows = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobPost> jobPosts = new ArrayList<>();
	
	@OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;
	
}

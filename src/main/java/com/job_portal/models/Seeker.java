package com.job_portal.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "seeker_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seeker {
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "address", length = 100)
    private String address;

    @ManyToOne
    @JoinColumn(name = "industry_id", nullable = true)
    private Industry industry;

    @Column(name = "gender", length = 100)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "email_contact", length = 50)
    private String emailContact;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;
    
    @ManyToMany
	private List<Skills> skills = new ArrayList<>();
    
    @OneToMany(mappedBy = "seeker", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SocialLink> socialLinks = new ArrayList<>();
}

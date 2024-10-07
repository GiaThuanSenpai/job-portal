package com.job_portal.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "apply_job")
@IdClass(IdApplyJob.class)
public class ApplyJob {

    @Id
    @Column(name = "post_id", columnDefinition = "BINARY(16)")
    private UUID postId;

    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "path_CV", length = 100)
    private String pathCV;

    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    @Column(name = "is_save")
    private boolean isSave;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Lob
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description;

    // Quan hệ với JobPosts
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", insertable = false, updatable = false)
    private JobPost jobPost;

    // Quan hệ với SeekerProfile
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private Seeker seeker;

    public ApplyJob() {
	}

	public ApplyJob(UUID postId, UUID userId, String pathCV, LocalDateTime applyDate, boolean isSave, String fullName,
			String email, String description, JobPost jobPost, Seeker seeker) {
		super();
		this.postId = postId;
		this.userId = userId;
		this.pathCV = pathCV;
		this.applyDate = applyDate;
		this.isSave = isSave;
		this.fullName = fullName;
		this.email = email;
		this.description = description;
		this.jobPost = jobPost;
		this.seeker = seeker;
	}

	public UUID getPostId() {
		return postId;
	}

	public void setPostId(UUID postId) {
		this.postId = postId;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getPathCV() {
		return pathCV;
	}

	public void setPathCV(String pathCV) {
		this.pathCV = pathCV;
	}

	public LocalDateTime getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(LocalDateTime applyDate) {
		this.applyDate = applyDate;
	}

	public boolean isSave() {
		return isSave;
	}

	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JobPost getJobPost() {
		return jobPost;
	}

	public void setJobPost(JobPost jobPost) {
		this.jobPost = jobPost;
	}

	public Seeker getSeeker() {
		return seeker;
	}

	public void setSeeker(Seeker seeker) {
		this.seeker = seeker;
	}
	
	
}
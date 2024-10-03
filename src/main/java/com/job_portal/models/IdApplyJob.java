package com.job_portal.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class IdApplyJob implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID postId;
    private UUID userId;

    // Default constructor
    public IdApplyJob() {}

    // Parameterized constructor
    public IdApplyJob(UUID postId, UUID userId) {
        this.postId = postId;
        this.userId = userId;
    }

    // Getters and Setters
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

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdApplyJob applyId = (IdApplyJob) o;

        return Objects.equals(postId, applyId.postId) &&
               Objects.equals(userId, applyId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
package com.job_portal.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class IdSocialLink implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID userId;
    private String socialName;

    // Constructors
    public IdSocialLink() {}

    public IdSocialLink(UUID userId, String socialName) {
        this.userId = userId;
        this.socialName = socialName;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    // Equals and HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdSocialLink)) return false;
        IdSocialLink that = (IdSocialLink) o;
        return Objects.equals(userId, that.userId) && Objects.equals(socialName, that.socialName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, socialName);
    }
}

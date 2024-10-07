package com.job_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.models.IdSocialLink;
import com.job_portal.models.SocialLink;

public interface SocialLinkRepository extends JpaRepository<SocialLink, IdSocialLink>{

}

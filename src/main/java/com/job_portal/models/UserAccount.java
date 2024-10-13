package com.job_portal.models;

import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_account")
public class UserAccount {

	private static final String DEFAULT_AVATAR_URL = "https://res.cloudinary.com/ddqygrb0g/image/upload/v1721653511/default-avatar_lxvn0y.jpg";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
    private UUID userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

    @Column(name = "is_active", columnDefinition = "BIT(1)")
    private boolean isActive;

    @Column(name = "user_name", length = 100, nullable = false)
    private String userName;

    @Column(name = "avatar", length = 200)
    private String avatar;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "create_date", nullable = true)
    private LocalDateTime createDate;
    
    @Column(name = "last_login", nullable = true)
    private LocalDateTime lastLogin;
    
    private String otp;
	private LocalDateTime otpGeneratedTime;
	
	@JsonIgnore
	@OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Seeker seeker;
	
	@JsonIgnore
	@OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Company company;
	
	@OneToOne(mappedBy = "userAccount")
	private ForgotPassword forgotPassword;

	public UserAccount(UUID userId, UserType userType, boolean isActive, String userName, String avatar, String email,
			String password, LocalDateTime createDate, LocalDateTime lastLogin, String otp,
			LocalDateTime otpGeneratedTime, Seeker seeker, Company company) {
		super();
		this.userId = userId;
		this.userType = userType;
		this.isActive = isActive;
		this.userName = userName;
		this.avatar = avatar;
		this.email = email;
		this.password = password;
		this.createDate = createDate;
		this.lastLogin = lastLogin;
		this.otp = otp;
		this.otpGeneratedTime = otpGeneratedTime;
		this.seeker = seeker;
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Seeker getSeeker() {
		return seeker;
	}

	public void setSeeker(Seeker seeker) {
		this.seeker = seeker;
	}

	// Constructors
    public UserAccount() {
    	this.avatar = DEFAULT_AVATAR_URL;
    	this.isActive = false; // Hoặc true tùy theo yêu cầu của bạn
    }
    


	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	// Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getOtpGeneratedTime() {
		return otpGeneratedTime;
	}

	public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
		this.otpGeneratedTime = otpGeneratedTime;
	}
    
    
}
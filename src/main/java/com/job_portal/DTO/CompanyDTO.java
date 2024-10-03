package com.job_portal.DTO;

import java.time.LocalDate;
import java.util.Date;

public class CompanyDTO {
	private String companyName;
    private Integer industryId; // Thay đổi từ industryId sang industryName
    private Integer cityId; // Thay đổi từ cityId sang cityName
    private String address;
    private String description;
    private String logo;
    private String contact;
    private String email;
    private LocalDate established_date;
    
    
	public CompanyDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CompanyDTO(String companyName, Integer industryId, Integer cityId, String address, String description,
			String logo, String contact, String email, LocalDate established_date) {
		super();
		this.companyName = companyName;
		this.industryId = industryId;
		this.cityId = cityId;
		this.address = address;
		this.description = description;
		this.logo = logo;
		this.contact = contact;
		this.email = email;
		this.established_date = established_date;
	}
	
	
	public Integer getIndustryId() {
		return industryId;
	}
	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDate getEstablished_date() {
		return established_date;
	}
	public void setEstablished_date(LocalDate established_date) {
		this.established_date = established_date;
	}
    
	
    
}

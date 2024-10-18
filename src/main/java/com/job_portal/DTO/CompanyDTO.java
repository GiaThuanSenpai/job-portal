package com.job_portal.DTO;

import java.time.LocalDate;

public class CompanyDTO {
    private String companyName;
    private Long applicationCount;
    private Integer industryId;
    private Integer cityId;
    private String address;
    private String description;
    private String logo;
    private String contact;
    private String email;
    private LocalDate establishedDate;

    public CompanyDTO() {
        // Constructor không tham số
    }

    public CompanyDTO(String companyName, Long applicationCount, Integer industryId, Integer cityId, String address,
                      String description, String logo, String contact, String email, LocalDate establishedDate) {
        this.companyName = companyName;
        this.applicationCount = applicationCount;
        this.industryId = industryId;
        this.cityId = cityId;
        this.address = address;
        this.description = description;
        this.logo = logo;
        this.contact = contact;
        this.email = email;
        this.establishedDate = establishedDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(Long applicationCount) {
        this.applicationCount = applicationCount;
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

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
                "companyName='" + companyName + '\'' +
                ", applicationCount=" + applicationCount +
                ", industryId=" + industryId +
                ", cityId=" + cityId +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", establishedDate=" + establishedDate +
                '}';
    }
}

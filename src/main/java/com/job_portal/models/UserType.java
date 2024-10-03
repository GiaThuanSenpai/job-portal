package com.job_portal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_type")
public class UserType {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_type_id")
	private Integer userTypeId;
	
	private String user_type_name;

	public UserType() {

	}




	public Integer getUserTypeId() {
		return userTypeId;
	}




	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}




	public String getUser_type_name() {
		return user_type_name;
	}

	public UserType(Integer userTypeId, String user_type_name) {
		super();
		this.userTypeId = userTypeId;
		this.user_type_name = user_type_name;
	}




	public void setUser_type_name(String user_type_name) {
		this.user_type_name = user_type_name;
	}
	
	
	
}

package com.job_portal.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class DateRangeDTO {
	private LocalDate startDate;
    private LocalDate endDate;
	public DateRangeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DateRangeDTO(LocalDate startDate, LocalDate endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
    
    
}

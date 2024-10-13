package com.job_portal.DTO;

import java.time.LocalDate;

public class DailyAccountCount {
    private LocalDate registrationDate;
    private Long count;

    // Constructor, Getters, and Setters
    public DailyAccountCount(LocalDate registrationDate, Long count) {
        this.registrationDate = registrationDate;
        this.count = count;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

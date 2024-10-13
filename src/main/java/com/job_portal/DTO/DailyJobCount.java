package com.job_portal.DTO;

import java.time.LocalDate;

public class DailyJobCount {
    private LocalDate date;
    private Long count;

    // Constructor, Getters, and Setters
    public DailyJobCount(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

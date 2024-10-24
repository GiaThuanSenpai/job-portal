package com.job_portal.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.job_portal.service.IJobPostService;

@Component
public class JobPostScheduler {

    @Autowired
    private IJobPostService jobPostService;

    // Cập nhật file CSV mỗi 5 phút
    @Scheduled(fixedRate = 300000)
    public void updateCSV() {
        try {
            jobPostService.exportJobPostToCSV("D:\\\\\\\\Job_portal-main\\\\\\\\job_post.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
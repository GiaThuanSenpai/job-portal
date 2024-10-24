package com.job_portal.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job_portal.models.SearchHistory;
import com.job_portal.repository.SearchHistoryRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.opencsv.CSVWriter;

@Service
public class SearchHistoryServiceImpl implements ISearchHistoryService {

	@Autowired
	private SearchHistoryRepository searchHistoryRepository;
	@Override
	public void exportSearchHistoryToCSV(String filePath) throws IOException {
		List<SearchHistory> searchHistories = searchHistoryRepository.findAll();
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Viết tiêu đề
            String[] header = {"ID", "SeekerID", "Search Query", "Search Date"};
            writer.writeNext(header);

            // Viết dữ liệu
            for (SearchHistory history : searchHistories) {
                String[] data = {
                    history.getHistoryId().toString(),
                    history.getSeeker() != null ? history.getSeeker().getUserId().toString() : "N/A", // Truy cập ID của seeker
                    history.getSearchQuery(),
                    history.getSearchDate().toString()
                };
                writer.writeNext(data);
            }

        }
	}
}

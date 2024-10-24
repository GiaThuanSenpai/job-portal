package com.job_portal.service;

import java.io.IOException;

public interface ISearchHistoryService {
	 public void exportSearchHistoryToCSV(String filePath) throws IOException;
}

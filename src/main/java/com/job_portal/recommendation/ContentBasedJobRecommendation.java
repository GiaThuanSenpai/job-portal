package com.job_portal.recommendation;

import java.util.List;
import java.util.stream.Collectors;


import com.job_portal.models.JobPost;

public class ContentBasedJobRecommendation {

//	public TFIDF createTFIDFModel(List<JobPost> jobPosts) {
//	    // Chuyển đổi JobPost thành danh sách văn bản
//	    List<String> documents = jobPosts.stream()
//	                                      .map(job -> job.getTitle() + " " + job.getDescription())
//	                                      .collect(Collectors.toList());
//
//	    // Chuyển đổi danh sách văn bản thành ma trận
//	    Matrix termDocumentMatrix = new DenseMatrix(documents.size(), 2); // Chọn số lượng đặc trưng cần thiết
//
//	    // Tính toán TF-IDF
//	    TFIDF tfidf = new TFIDF(termDocumentMatrix);
//	    tfidf.calculate();
//
//	    return tfidf;
//	}
}

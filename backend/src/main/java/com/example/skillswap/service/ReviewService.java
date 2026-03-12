package com.example.skillswap.service;

import java.util.List;

import com.example.skillswap.model.Review;

public interface ReviewService {

    Review addReview(Review review, String email);

    List<Review> getReviewsByUser(String userId);

    double getAverageRating(String userId);

    void deleteReview(String reviewId);
    
    List<Review> getAllReviews();
}
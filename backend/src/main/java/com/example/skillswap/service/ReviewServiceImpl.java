package com.example.skillswap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skillswap.model.Review;
import com.example.skillswap.model.User;
import com.example.skillswap.repository.ReviewRepository;
import com.example.skillswap.repository.UserRepository;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Review addReview(Review review, String email) {

        // get logged in user
        User reviewer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // set reviewerId automatically
        review.setId(reviewer.getId());

        return reviewRepository.save(review);
    }
    
    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByUser(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public double getAverageRating(String userId) {

        List<Review> reviews = reviewRepository.findByUserId(userId);

        if (reviews.isEmpty())
            return 0.0;

        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();

        return sum / reviews.size();
    }

    @Override
    public void deleteReview(String reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
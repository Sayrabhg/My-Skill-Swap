package com.example.skillswap.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.skillswap.model.Review;
import com.example.skillswap.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Add a review
    @PostMapping("/add/{userId}")
    public Review addReview(@PathVariable String userId,
                            @RequestBody Review review,
                            Principal principal) {

        String email = principal.getName();

        review.setUserId(userId);

        return reviewService.addReview(review, email);
    }
    
    @GetMapping("/all")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // Get all reviews for a user
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable String userId) {
        return reviewService.getReviewsByUser(userId);
    }

    // Get average rating for a user
    @GetMapping("/user/{userId}/average")
    public double getAverageRating(@PathVariable String userId) {
        return reviewService.getAverageRating(userId);
    }

    // Delete a review
    @DeleteMapping("/{id}")
    public String deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return "Review deleted successfully";
    }
}
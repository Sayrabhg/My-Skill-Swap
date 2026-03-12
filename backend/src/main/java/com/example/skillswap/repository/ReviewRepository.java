package com.example.skillswap.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.skillswap.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByUserId(String userId);

}
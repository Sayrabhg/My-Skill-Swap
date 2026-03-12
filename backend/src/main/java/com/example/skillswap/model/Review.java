package com.example.skillswap.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "reviews")
@Data
public class Review {

    @Id
    private String id;

    private String userId;

    private int rating;
    private String comment;

}
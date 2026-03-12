package com.example.skillswap.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "skill_requests")
@Data
public class SkillRequest {

    @Id
    private String id;

    private String requesterId;
    private String mentorId;

    private String skillOffered;
    private String skillWanted;

    private String status; // PENDING ACCEPTED REJECTED

}

// example
// Saurabh → requests Spring Boot from John
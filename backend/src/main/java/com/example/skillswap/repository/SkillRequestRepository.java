package com.example.skillswap.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.skillswap.model.SkillRequest;

public interface SkillRequestRepository extends MongoRepository<SkillRequest, String> {

    List<SkillRequest> findByMentorId(String mentorId);

    List<SkillRequest> findByRequesterId(String requesterId);

}
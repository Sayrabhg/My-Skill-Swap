package com.example.skillswap.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.skillswap.model.SwapSession;

public interface SwapRepository extends MongoRepository<SwapSession, String> {

    List<SwapSession> findByMentorId(String mentorId);
    List<SwapSession> findByStudentId(String studentId);
}

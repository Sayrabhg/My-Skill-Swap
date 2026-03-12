package com.example.skillswap.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.skillswap.model.Skill;

public interface SkillRepository extends MongoRepository<Skill, String> {

    List<Skill> findByUserId(String userId);

}
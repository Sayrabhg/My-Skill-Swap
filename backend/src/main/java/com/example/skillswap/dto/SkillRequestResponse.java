package com.example.skillswap.dto;

import com.example.skillswap.model.SkillRequest;
import com.example.skillswap.model.User;

import lombok.Data;

@Data
public class SkillRequestResponse {

    private SkillRequest request;
    private User mentor;
}
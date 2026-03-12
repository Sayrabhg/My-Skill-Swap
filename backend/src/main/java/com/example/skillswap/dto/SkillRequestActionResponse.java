package com.example.skillswap.dto;

import com.example.skillswap.model.SkillRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillRequestActionResponse {
    private String message;
    private SkillRequest request;
}
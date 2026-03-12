package com.example.skillswap.dto;

import java.util.List;

import com.example.skillswap.model.Skill;
import com.example.skillswap.model.User;

import lombok.Data;

@Data
public class UserProfileDTO {

    private User user;
    private List<Skill> skills;

}
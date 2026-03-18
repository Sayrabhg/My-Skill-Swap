package com.example.skillswap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skillswap.dto.UserProfileDTO;
import com.example.skillswap.dto.UserUpdateRequest;
import com.example.skillswap.model.Skill;
import com.example.skillswap.model.User;
import com.example.skillswap.repository.SkillRepository;
import com.example.skillswap.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SkillRepository skillRepository;

    // ---------------- GET ALL USERS (ADMIN) ----------------
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ---------------- GET USER BY ID (ADMIN) ----------------
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // ---------------- GET USER BY EMAIL (JWT) ----------------
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ---------------- UPDATE USER BY ID (ADMIN) ----------------
    public User updateUserByEmail(String email, UserUpdateRequest updatedUser) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return null;

        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getBio() != null) user.setBio(updatedUser.getBio());
        if (updatedUser.getLanguage() != null) user.setLanguage(updatedUser.getLanguage());
        if (updatedUser.getAvatar() != null) user.setAvatar(updatedUser.getAvatar());

        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getMobileNumber() != null) user.setMobileNumber(updatedUser.getMobileNumber());

        if (updatedUser.getGender() != null) user.setGender(updatedUser.getGender());

        if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
        if (updatedUser.getCity() != null) user.setCity(updatedUser.getCity());
        if (updatedUser.getState() != null) user.setState(updatedUser.getState());
        if (updatedUser.getCountry() != null) user.setCountry(updatedUser.getCountry());
        if (updatedUser.getPostalCode() != null) user.setPostalCode(updatedUser.getPostalCode());

        if (updatedUser.getWebsite() != null) user.setWebsite(updatedUser.getWebsite());
        if (updatedUser.getLinkedin() != null) user.setLinkedin(updatedUser.getLinkedin());
        if (updatedUser.getGithub() != null) user.setGithub(updatedUser.getGithub());

        if (updatedUser.getIsFirstLogin() != null) {
            user.setFirstLogin(updatedUser.getIsFirstLogin());
        }
        return userRepository.save(user);
    }

    // ---------------- UPDATE CURRENT USER BY EMAIL (JWT) ----------------
    public User updateUserByEmail(String email, User updatedUser) {
        Optional<User> existingOpt = userRepository.findByEmail(email);
        if (existingOpt.isPresent()) {
            User existing = existingOpt.get();
            copyUserFields(existing, updatedUser);
            return userRepository.save(existing);
        }
        return null;
    }

    // ---------------- DELETE USER BY ID (ADMIN) ----------------
    public boolean deleteUser(String id) {
        Optional<User> existingOpt = userRepository.findById(id);
        if (existingOpt.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ---------------- DELETE CURRENT USER BY EMAIL (JWT) ----------------
    public boolean deleteUserByEmail(String email) {
        Optional<User> existingOpt = userRepository.findByEmail(email);
        if (existingOpt.isPresent()) {
            userRepository.delete(existingOpt.get());
            return true;
        }
        return false;
    }

    // ---------------- HELPER: copy fields ----------------
    private void copyUserFields(User existing, User updated) {
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getLanguage() != null) existing.setLanguage(updated.getLanguage());
        if (updated.getBio() != null) existing.setBio(updated.getBio());
        if (updated.getAvatar() != null) existing.setAvatar(updated.getAvatar());

        // Contact info
        if (updated.getMobileNumber() != null) existing.setMobileNumber(updated.getMobileNumber());
        
        // Personal info
        if (updated.getGender() != null) existing.setGender(updated.getGender());

        // Address info
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());
        if (updated.getCity() != null) existing.setCity(updated.getCity());
        if (updated.getState() != null) existing.setState(updated.getState());
        if (updated.getCountry() != null) existing.setCountry(updated.getCountry());
        if (updated.getPostalCode() != null) existing.setPostalCode(updated.getPostalCode());

        // Optional / social links
        if (updated.getWebsite() != null) existing.setWebsite(updated.getWebsite());
        if (updated.getLinkedin() != null) existing.setLinkedin(updated.getLinkedin());
        if (updated.getGithub() != null) existing.setGithub(updated.getGithub());

        // Do NOT update: email, tokens, rating, trustScore (only admin)
    }
    
    public UserProfileDTO getUserProfile(String userId) {

        User user = userRepository.findById(userId).orElseThrow();
        List<Skill> skills = skillRepository.findByUserId(userId);

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUser(user);
        dto.setSkills(skills);

        return dto;
    }
}
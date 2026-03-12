package com.example.skillswap.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skillswap.dto.SkillRequestResponse;
import com.example.skillswap.model.SkillRequest;
import com.example.skillswap.model.User;
import com.example.skillswap.repository.SkillRequestRepository;
import com.example.skillswap.repository.UserRepository;

@Service
public class SkillRequestService {

    @Autowired
    private SkillRequestRepository requestRepository;
    
    @Autowired
    private UserRepository userRepository;

    public SkillRequest sendRequest(SkillRequest request, String email) {

        // Get requester user
        User user = userRepository.findByEmail(email).orElseThrow();

        if(user.getTokens() < 5){
            throw new RuntimeException("Not enough tokens");
        }

        // Deduct tokens
        user.setTokens(user.getTokens() - 5);
        userRepository.save(user);

        // Set requesterId automatically
        request.setRequesterId(user.getId());
        request.setStatus("PENDING");

        // Validate mentor
        User mentor = userRepository.findById(request.getMentorId())
                        .orElseThrow(() -> new RuntimeException("Mentor not found"));

        if(!mentor.getRole().equals("MENTOR")){
            throw new RuntimeException("Selected user is not a mentor");
        }

        return requestRepository.save(request);
    }

    // Populate mentor details
    public List<SkillRequestResponse> getRequestsForMentor(String mentorId) {

        List<SkillRequest> requests = requestRepository.findByMentorId(mentorId);

        List<SkillRequestResponse> responseList = new ArrayList<>();

        for(SkillRequest req : requests){

            User mentor = userRepository.findById(req.getMentorId()).orElse(null);

            SkillRequestResponse res = new SkillRequestResponse();
            res.setRequest(req);
            res.setMentor(mentor);

            responseList.add(res);
        }

        return responseList;
    }

    public SkillRequest acceptRequest(String requestId) {

        SkillRequest request = requestRepository.findById(requestId).orElse(null);

        if (request != null) {
            request.setStatus("ACCEPTED");
            return requestRepository.save(request);
        }

        return null;
    }

    public SkillRequest rejectRequest(String requestId) {

        SkillRequest request = requestRepository.findById(requestId).orElse(null);

        if (request != null) {
            request.setStatus("REJECTED");
            return requestRepository.save(request);
        }

        return null;
    }
}
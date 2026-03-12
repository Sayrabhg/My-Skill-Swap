package com.example.skillswap.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.skillswap.dto.SkillRequestActionResponse;
import com.example.skillswap.dto.SkillRequestResponse;
import com.example.skillswap.model.SkillRequest;
import com.example.skillswap.service.SkillRequestService;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin("*")
public class SkillRequestController {

    @Autowired
    private SkillRequestService requestService;

    // Send request to a specific mentor by path variable
    @PostMapping("/send/{mentorId}")
    public SkillRequest sendRequest(@PathVariable String mentorId, @RequestBody SkillRequest request, Principal principal) {

        // Get logged-in user email from JWT
        String email = principal.getName();

        // Set mentorId in the request
        request.setMentorId(mentorId);

        // Send to service
        return requestService.sendRequest(request, email);
    }

    @GetMapping("/mentor/{mentorId}")
    public List<SkillRequestResponse> getMentorRequests(@PathVariable String mentorId){
        return requestService.getRequestsForMentor(mentorId);
    }

    @PutMapping("/accept/{requestId}")
    public SkillRequestActionResponse acceptRequest(@PathVariable String requestId) {
        SkillRequest request = requestService.acceptRequest(requestId);
        return new SkillRequestActionResponse("Request accepted successfully", request);
    }

    @PutMapping("/reject/{requestId}")
    public SkillRequestActionResponse rejectRequest(@PathVariable String requestId) {
        SkillRequest request = requestService.rejectRequest(requestId);
        return new SkillRequestActionResponse("Request rejected", request);
    }
}
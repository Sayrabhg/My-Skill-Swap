package com.example.skillswap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.skillswap.model.SwapSession;
import com.example.skillswap.service.SwapSessionService;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin("*")
public class SwapController {

    @Autowired
    private SwapSessionService sessionService;

    // Create a session (pass mentorId and studentId in path)
    @PostMapping("/create/{mentorId}/{studentId}")
    public SwapSession createSession(@PathVariable String mentorId,
                                     @PathVariable String studentId,
                                     @RequestBody SwapSession session) {
        session.setMentorId(mentorId);
        session.setStudentId(studentId);
        return sessionService.createSession(session);
    }

    // Get sessions by mentor
    @GetMapping("/mentor/{mentorId}")
    public List<SwapSession> getSessionsByMentor(@PathVariable String mentorId) {
        return sessionService.getSessionsByMentor(mentorId);
    }

    // Get sessions by student
    @GetMapping("/student/{studentId}")
    public List<SwapSession> getSessionsByStudent(@PathVariable String studentId) {
        return sessionService.getSessionsByStudent(studentId);
    }

    // Update session status
    @PutMapping("/updateStatus/{sessionId}")
    public SwapSession updateStatus(@PathVariable String sessionId, @RequestParam String status) {
        return sessionService.updateSessionStatus(sessionId, status);
    }

    // Get all sessions
    @GetMapping("/all")
    public List<SwapSession> getAllSessions() {
        return sessionService.getAllSessions();
    }
}
package com.example.skillswap.service;

import java.util.List;

import com.example.skillswap.model.SwapSession;

public interface SwapSessionService {
    SwapSession createSession(SwapSession session);
    List<SwapSession> getSessionsByMentor(String mentorId);
    List<SwapSession> getSessionsByStudent(String studentId);
    SwapSession updateSessionStatus(String sessionId, String status);
    List<SwapSession> getAllSessions();
}
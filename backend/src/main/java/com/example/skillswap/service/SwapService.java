package com.example.skillswap.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skillswap.model.SwapSession;
import com.example.skillswap.repository.SwapRepository;

@Service
public class SwapService implements SwapSessionService {

    @Autowired
    private SwapRepository swapRepository;

    @Override
    public SwapSession createSession(SwapSession session) {
        session.setStatus("PENDING"); // default status
        return swapRepository.save(session);
    }

    @Override
    public List<SwapSession> getSessionsByMentor(String mentorId) {
        return swapRepository.findByMentorId(mentorId);
    }

    @Override
    public List<SwapSession> getSessionsByStudent(String studentId) {
        return swapRepository.findByStudentId(studentId);
    }

    @Override
    public SwapSession updateSessionStatus(String id, String status) {
        SwapSession session = swapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found")); // <-- works now
        session.setStatus(status.toUpperCase());
        return swapRepository.save(session);
    }

    @Override
    public List<SwapSession> getAllSessions() {
        return swapRepository.findAll();
    }
}
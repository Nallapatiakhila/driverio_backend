package com.example.rollbasedlogin.controller;

import com.example.rollbasedlogin.model.Booking;
import com.example.rollbasedlogin.repository.BookingRepository;
import com.example.rollbasedlogin.service.AiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AiService aiService;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/ask")
    public Object askQuestion(@RequestBody QuestionRequest request) {

        String question = request.getQuestion().toLowerCase();

        // Simple smart filters

        if (question.contains("car") || question.contains("cab")) {
            return bookingRepository.findByCabType("Cab");
        }

        if (question.contains("van")) {
            return bookingRepository.findByCabType("Van");
        }

        if (question.contains("all")) {
            return bookingRepository.findAll();
        }

        // If nothing matches → send to HuggingFace AI
        return aiService.askAI(question);
    }
}

class QuestionRequest {

    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
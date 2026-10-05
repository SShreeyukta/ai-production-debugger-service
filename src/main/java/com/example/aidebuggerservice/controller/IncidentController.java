package com.example.aidebuggerservice.controller;

import com.example.aidebuggerservice.model.AIAnalysis;
import com.example.aidebuggerservice.model.Incident;
import com.example.aidebuggerservice.service.OllamaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final OllamaService ollamaService;

    public IncidentController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping
    public Incident createIncident(@RequestBody Incident incident) {
        return incident;
    }

    @GetMapping("/health")
    public String health() {
        return "AI Debugger Service is healthy";
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyze(@RequestBody Incident incident) {

        AIAnalysis analysis = ollamaService.analyze(incident);

        return Map.of(
                "service", incident.getService(),
                "orderId", incident.getOrderId(),
                "aiAnalysis", analysis
        );
    }
}
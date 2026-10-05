package com.example.aidebuggerservice.controller;

import com.example.aidebuggerservice.model.AIAnalysis;
import com.example.aidebuggerservice.model.Incident;
import com.example.aidebuggerservice.model.IncidentRecord;
import com.example.aidebuggerservice.repository.IncidentRecordRepository;
import com.example.aidebuggerservice.service.OllamaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final OllamaService ollamaService;
    private final IncidentRecordRepository repository;

    public IncidentController(
            OllamaService ollamaService,
            IncidentRecordRepository repository) {
        this.ollamaService = ollamaService;
        this.repository = repository;
    }

    @PostMapping
    public Map<String, Object> createIncident(
            @RequestBody Incident incident) {

        AIAnalysis analysis = ollamaService.analyze(incident);

        IncidentRecord record = new IncidentRecord();

        record.setService(incident.getService());
        record.setOrderId(incident.getOrderId());
        record.setStatus(incident.getStatus());
        record.setError(incident.getError());

        record.setRootCause(analysis.getRootCause());
        record.setHypothesis(analysis.getHypothesis());
        record.setConfidence(analysis.getConfidence());

        record.setEvidence(String.join("\n", analysis.getEvidence()));
        record.setMissingEvidence(
                String.join("\n", analysis.getMissingEvidence()));
        record.setRecommendedActions(
                String.join("\n", analysis.getRecommendedActions()));

        IncidentRecord saved = repository.save(record);

        return Map.of(
                "incidentId", saved.getId(),
                "service", incident.getService(),
                "orderId", incident.getOrderId(),
                "status", incident.getStatus(),
                "aiAnalysis", analysis
        );
    }

    @GetMapping
    public java.util.List<IncidentRecord> getIncidents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public IncidentRecord getIncident(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    @GetMapping("/health")
    public String health() {
        return "AI Debugger Service is healthy";
    }
}
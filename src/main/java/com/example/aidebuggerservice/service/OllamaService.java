package com.example.aidebuggerservice.service;

import com.example.aidebuggerservice.model.AIAnalysis;
import com.example.aidebuggerservice.model.Incident;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class OllamaService {

    private final RestClient restClient;
    private final VectorStore vectorStore;

    public OllamaService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:11434")
                .requestFactory(new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(10))
                                .build()
                ))
                .build();
    }

    public AIAnalysis analyze(Incident incident) {

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(incident.getError())
                        .topK(3)
                        .similarityThreshold(0.7)
                        .build()
        );

        String retrievedContext = results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        String prompt = """
        You are an expert production reliability engineer.

        Investigate this production incident.

        CURRENT INCIDENT:
        Service: %s
        Error: %s
        Order ID: %s
        Status: %s

        HISTORICAL INCIDENTS RETRIEVED FROM THE KNOWLEDGE BASE:
        %s

        IMPORTANT RULES:
        1. Treat current incident data as observed facts.
        2. Treat retrieved historical incidents only as supporting evidence.
        3. A historical incident can suggest a hypothesis, but it cannot establish the current root cause.
        4. Never infer a specific mechanism from a generic error message.
           For example, "database connection timeout" does NOT prove:
           - connection pool exhaustion
           - leaked connections
           - database saturation
           - network failure
        5. Only claim a specific root cause when current evidence supports that mechanism.
        6. If current evidence is insufficient, say:
           "Root cause cannot be confirmed from the available evidence."
        7. Clearly separate:
           - Observed evidence
           - Historical evidence
           - Hypothesis
           - Missing evidence
        8. Do not invent logs, metrics, traces, payloads, or system behavior.
        9. Keep the final response concise and operational.
           Do not explain these rules or repeat the prompt.

                OUTPUT FORMAT:
                
                Return ONLY valid JSON.
                
                Do not use markdown.
                Do not use ``` code fences.
                Do not add any text before or after the JSON.
                
                Use exactly this structure:
                
                {
                  "rootCause": "confirmed root cause or Not confirmed",
                  "hypothesis": "most likely explanation",
                  "confidence": "Low",
                  "evidence": [
                    "observed or retrieved evidence"
                  ],
                  "missingEvidence": [
                    "evidence needed to confirm the hypothesis"
                  ],
                  "recommendedActions": [
                    "action 1",
                    "action 2",
                    "action 3"
                  ]
                }
        """.formatted(
                incident.getService(),
                incident.getError(),
                incident.getOrderId(),
                incident.getStatus(),
                retrievedContext
        );

        Map<String, Object> request = Map.of(
                "model", "qwen3:4b",
                "prompt", prompt,
                "stream", false
        );

        Map<?, ?> response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(Map.class);

        String aiResponse = (String) response.get("response");

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(aiResponse, AIAnalysis.class);
    }
}
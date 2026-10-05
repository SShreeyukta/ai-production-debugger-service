package com.example.aidebuggerservice.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final VectorStore vectorStore;

    public KnowledgeController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostMapping("/load")
    public Map<String, String> loadKnowledge() {

        List<Document> documents = List.of(
                new Document("""
                    Incident: Payment database connection timeout.
                    Service: payment-service.
                    Root cause: Database connection pool exhaustion caused by leaked connections.
                    Resolution: Fixed connection lifecycle and increased connection pool monitoring.
                    """),

                new Document("""
                    Incident: Payment Kafka consumer repeatedly failed.
                    Service: payment-service.
                    Root cause: Invalid event payload caused deserialization failure.
                    Resolution: Corrected event schema and added consumer-side validation.
                    """),

                new Document("""
                    Incident: Inventory service experienced database timeout.
                    Service: inventory-service.
                    Root cause: Database became unreachable during a connection saturation event.
                    Resolution: Restored database connectivity and tuned connection limits.
                    """),

                new Document("""
                    Incident: Kafka messages were repeatedly retried.
                    Service: payment-service.
                    Root cause: Downstream payment processing was failing, causing Kafka consumer retries.
                    Resolution: Fixed the downstream dependency and verified successful event processing.
                    """)
        );

        vectorStore.add(documents);

        return Map.of(
                "status", "Knowledge loaded",
                "documents", String.valueOf(documents.size())
        );
    }
}
package com.example.aidebuggerservice.model;

public class Incident {

    private String service;
    private String error;
    private Long orderId;
    private String status;
    private String stackTrace;
    private String logs;
    private String kafkaTopic;
    private Integer kafkaPartition;
    private Long kafkaOffset;

    public Incident() {
    }

    public Incident(String service, String error, Long orderId, String status) {
        this.service = service;
        this.error = error;
        this.orderId = orderId;
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public String getError() {
        return error;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getLogs() {
        return logs;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public Long getKafkaOffset() {
        return kafkaOffset;
    }

    public Integer getKafkaPartition() {
        return kafkaPartition;
    }
}
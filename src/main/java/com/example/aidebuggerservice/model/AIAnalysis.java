package com.example.aidebuggerservice.model;

import java.util.List;

public class AIAnalysis {

    private String rootCause;
    private String hypothesis;
    private String confidence;
    private List<String> evidence;
    private List<String> missingEvidence;
    private List<String> recommendedActions;

    public AIAnalysis() {
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public List<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<String> evidence) {
        this.evidence = evidence;
    }

    public List<String> getMissingEvidence() {
        return missingEvidence;
    }

    public void setMissingEvidence(List<String> missingEvidence) {
        this.missingEvidence = missingEvidence;
    }

    public List<String> getRecommendedActions() {
        return recommendedActions;
    }

    public void setRecommendedActions(List<String> recommendedActions) {
        this.recommendedActions = recommendedActions;
    }
}
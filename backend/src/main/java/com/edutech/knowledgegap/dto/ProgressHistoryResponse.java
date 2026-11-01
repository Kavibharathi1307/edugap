package com.edutech.knowledgegap.dto;

import java.util.List;
import java.util.Map;

public class ProgressHistoryResponse {

    private List<String> labels; // e.g. "Attempt 1 (Oct 28)", "Attempt 2 (Oct 28)"
    private List<Double> overallScores;
    private Map<String, List<Double>> topicScoresTrend;
    private Map<String, List<Double>> subjectScoresTrend;
    private Double improvementPercentage;

    public ProgressHistoryResponse() {}

    public ProgressHistoryResponse(List<String> labels, List<Double> overallScores, Map<String, List<Double>> topicScoresTrend) {
        this(labels, overallScores, topicScoresTrend, Map.of(), null);
    }

    public ProgressHistoryResponse(List<String> labels, List<Double> overallScores,
                                   Map<String, List<Double>> topicScoresTrend,
                                   Map<String, List<Double>> subjectScoresTrend,
                                   Double improvementPercentage) {
        this.labels = labels;
        this.overallScores = overallScores;
        this.topicScoresTrend = topicScoresTrend;
        this.subjectScoresTrend = subjectScoresTrend;
        this.improvementPercentage = improvementPercentage;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Double> getOverallScores() {
        return overallScores;
    }

    public void setOverallScores(List<Double> overallScores) {
        this.overallScores = overallScores;
    }

    public Map<String, List<Double>> getTopicScoresTrend() {
        return topicScoresTrend;
    }

    public void setTopicScoresTrend(Map<String, List<Double>> topicScoresTrend) {
        this.topicScoresTrend = topicScoresTrend;
    }

    public Map<String, List<Double>> getSubjectScoresTrend() {
        return subjectScoresTrend;
    }

    public void setSubjectScoresTrend(Map<String, List<Double>> subjectScoresTrend) {
        this.subjectScoresTrend = subjectScoresTrend;
    }

    public Double getImprovementPercentage() {
        return improvementPercentage;
    }

    public void setImprovementPercentage(Double improvementPercentage) {
        this.improvementPercentage = improvementPercentage;
    }
}

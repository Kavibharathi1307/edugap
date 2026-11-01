package com.edutech.knowledgegap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.gap")
public class PerformanceThresholds {

    private double highPriorityThreshold = 40.0;
    private double needsImprovementThreshold = 70.0;
    private double goodUnderstandingThreshold = 85.0;

    public double getHighPriorityThreshold() {
        return highPriorityThreshold;
    }

    public void setHighPriorityThreshold(double highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    public double getNeedsImprovementThreshold() {
        return needsImprovementThreshold;
    }

    public void setNeedsImprovementThreshold(double needsImprovementThreshold) {
        this.needsImprovementThreshold = needsImprovementThreshold;
    }

    public double getGoodUnderstandingThreshold() {
        return goodUnderstandingThreshold;
    }

    public void setGoodUnderstandingThreshold(double goodUnderstandingThreshold) {
        this.goodUnderstandingThreshold = goodUnderstandingThreshold;
    }
}

package com.edutech.knowledgegap;

import com.edutech.knowledgegap.config.PerformanceThresholds;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PerformanceThresholds.class)
public class KnowledgeGapApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeGapApplication.class, args);
    }
}

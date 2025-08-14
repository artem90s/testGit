package com.example.reportservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "report")
public class ReportProperties {
    private Map<String, String> templates = new HashMap<>();
    private String defaultTemplateDir = "classpath:reports";

    public Map<String, String> getTemplates() {
        return templates;
    }

    public void setTemplates(Map<String, String> templates) {
        this.templates = templates;
    }

    public String getDefaultTemplateDir() {
        return defaultTemplateDir;
    }

    public void setDefaultTemplateDir(String defaultTemplateDir) {
        this.defaultTemplateDir = defaultTemplateDir;
    }
}
package com.example.reportservice.service.dto;

import com.example.reportservice.service.export.ReportFormat;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateReportRequest {
    @NotNull
    private ReportFormat format;

    private Map<String, Object> parameters = new HashMap<>();

    private List<Map<String, Object>> items = new ArrayList<>();

    private String fileName;

    public ReportFormat getFormat() {
        return format;
    }

    public void setFormat(ReportFormat format) {
        this.format = format;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
package com.example.reportservice.service;

import com.example.reportservice.service.dto.GenerateReportRequest;
import com.example.reportservice.service.dto.GeneratedReport;

public interface ReportService {
    GeneratedReport generate(String reportType, GenerateReportRequest request);
}
package com.example.reportservice.api;

import com.example.reportservice.service.ReportService;
import com.example.reportservice.service.dto.GenerateReportRequest;
import com.example.reportservice.service.dto.GeneratedReport;
import com.example.reportservice.service.export.ReportFormat;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{reportType}")
    public ResponseEntity<byte[]> generateGet(
            @PathVariable String reportType,
            @RequestParam(name = "format", required = false, defaultValue = "PDF") String formatParam,
            @RequestParam Map<String, String> allParams
    ) {
        Map<String, Object> parameters = new HashMap<>(allParams);
        parameters.remove("format");

        GenerateReportRequest request = new GenerateReportRequest();
        request.setFormat(ReportFormat.fromString(formatParam));
        request.setParameters(parameters);
        request.setItems(null);

        GeneratedReport report = reportService.generate(reportType, request);
        return buildAttachmentResponse(report);
    }

    @PostMapping("/{reportType}")
    public ResponseEntity<byte[]> generatePost(
            @PathVariable String reportType,
            @Valid @RequestBody GenerateReportRequest request
    ) {
        GeneratedReport report = reportService.generate(reportType, request);
        return buildAttachmentResponse(report);
    }

    private ResponseEntity<byte[]> buildAttachmentResponse(GeneratedReport report) {
        String contentDisposition = contentDispositionAttachment(report.getFileName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(report.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(report.getContent());
    }

    private String contentDispositionAttachment(String fileName) {
        String encoded = org.springframework.web.util.UriUtils.encode(fileName, StandardCharsets.UTF_8);
        return "attachment; filename=\"" + encoded + "\"";
    }
}
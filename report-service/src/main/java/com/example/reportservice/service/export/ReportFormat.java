package com.example.reportservice.service.export;

public enum ReportFormat {
    PDF,
    XLSX,
    CSV,
    DOCX;

    public static ReportFormat fromString(String value) {
        if (value == null) {
            return PDF;
        }
        for (ReportFormat format : values()) {
            if (format.name().equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unsupported report format: " + value);
    }
}
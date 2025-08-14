package com.example.reportservice.service.impl;

import com.example.reportservice.config.ReportProperties;
import com.example.reportservice.service.ReportService;
import com.example.reportservice.service.dto.GenerateReportRequest;
import com.example.reportservice.service.dto.GeneratedReport;
import com.example.reportservice.service.export.ReportExporter;
import com.example.reportservice.service.export.ReportFormat;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class JasperReportService implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(JasperReportService.class);

    private final ReportProperties properties;
    private final ResourceLoader resourceLoader;
    private final Map<String, JasperReport> templateCache = new ConcurrentHashMap<>();
    private final Map<ReportFormat, ReportExporter> exportersByFormat;

    public JasperReportService(ReportProperties properties,
                               ResourceLoader resourceLoader,
                               List<ReportExporter> exporters) {
        this.properties = properties;
        this.resourceLoader = resourceLoader;
        this.exportersByFormat = exporters.stream().collect(Collectors.toMap(
                ReportExporter::getFormat,
                e -> e
        ));
    }

    @Override
    public GeneratedReport generate(String reportType, GenerateReportRequest request) {
        try {
            JasperReport jasperReport = loadAndCompile(reportType);

            Map<String, Object> parameters = Optional.ofNullable(request.getParameters()).orElseGet(HashMap::new);

            JRDataSource dataSource;
            if (request.getItems() != null && !request.getItems().isEmpty()) {
                java.util.Collection<java.util.Map<java.lang.String, ?>> items = request.getItems().stream()
                        .map(m -> (java.util.Map<java.lang.String, ?>)(java.util.Map<?, ?>) m)
                        .collect(java.util.stream.Collectors.toList());
                dataSource = new JRMapCollectionDataSource(items);
            } else {
                dataSource = new JREmptyDataSource(1);
            }

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            ReportExporter exporter = resolveExporter(request.getFormat());
            byte[] content = exporter.export(print);

            String safeBaseName = StringUtils.hasText(request.getFileName()) ? request.getFileName() : buildDefaultFileName(reportType, request.getFormat());
            String fileName = ensureExtension(safeBaseName, exporter.getFileExtension());

            return new GeneratedReport(fileName, exporter.getContentType(), content);
        } catch (JRException e) {
            String message = "Failed to generate report '%s' in format %s".formatted(reportType, request.getFormat());
            log.error(message, e);
            throw new ReportGenerationException(message, e);
        }
    }

    private String ensureExtension(String baseName, String extension) {
        if (baseName.toLowerCase(Locale.ROOT).endsWith("." + extension.toLowerCase(Locale.ROOT))) {
            return baseName;
        }
        return baseName + "." + extension;
    }

    private String buildDefaultFileName(String reportType, ReportFormat format) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return reportType + "_" + ts + "." + format.name().toLowerCase(Locale.ROOT);
    }

    private JasperReport loadAndCompile(String reportType) throws JRException {
        String location = resolveTemplateLocation(reportType);
        try {
            return templateCache.computeIfAbsent(location, loc -> {
                try (InputStream is = openResource(loc)) {
                    return JasperCompileManager.compileReport(is);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof JRException jr) {
                throw jr;
            }
            throw new JRException("Failed to compile template: " + location, ex);
        }
    }

    private String resolveTemplateLocation(String reportType) {
        String configured = properties.getTemplates().get(reportType);
        if (StringUtils.hasText(configured)) {
            return configured;
        }
        String dir = properties.getDefaultTemplateDir();
        String prefix = dir.endsWith("/") ? dir : dir + "/";
        return prefix + reportType + ".jrxml";
    }

    private InputStream openResource(String location) {
        try {
            Resource resource = resourceLoader.getResource(location);
            if (!resource.exists()) {
                throw new IllegalArgumentException("Template not found: " + location);
            }
            return resource.getInputStream();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot open template: " + location, e);
        }
    }

    private ReportExporter resolveExporter(ReportFormat format) {
        ReportExporter exporter = exportersByFormat.get(format);
        if (exporter == null) {
            throw new IllegalArgumentException("No exporter for format: " + format);
        }
        return exporter;
    }

    public static class ReportGenerationException extends RuntimeException {
        public ReportGenerationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
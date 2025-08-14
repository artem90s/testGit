package com.example.reportservice.service.export.impl;

import com.example.reportservice.service.export.ReportExporter;
import com.example.reportservice.service.export.ReportFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@Component
public class CsvReportExporter implements ReportExporter {
    @Override
    public ReportFormat getFormat() {
        return ReportFormat.CSV;
    }

    @Override
    public String getContentType() {
        return "text/csv";
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }

    @Override
    public byte[] export(JasperPrint jasperPrint) throws JRException {
        JRCsvExporter exporter = new JRCsvExporter();
        SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
        configuration.setFieldDelimiter(",");
        configuration.setRecordDelimiter("\n");

        exporter.setConfiguration(configuration);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            exporter.setExporterOutput(new SimpleWriterExporterOutput(writer));
            exporter.exportReport();
            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            if (e instanceof JRException) {
                throw (JRException) e;
            }
            throw new JRException("Failed to export CSV", e);
        }
    }
}
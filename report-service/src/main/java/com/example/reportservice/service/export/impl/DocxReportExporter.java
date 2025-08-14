package com.example.reportservice.service.export.impl;

import com.example.reportservice.service.export.ReportExporter;
import com.example.reportservice.service.export.ReportFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class DocxReportExporter implements ReportExporter {
    @Override
    public ReportFormat getFormat() {
        return ReportFormat.DOCX;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    @Override
    public String getFileExtension() {
        return "docx";
    }

    @Override
    public byte[] export(JasperPrint jasperPrint) throws JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        SimpleDocxReportConfiguration configuration = new SimpleDocxReportConfiguration();
        exporter.setConfiguration(configuration);
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
            exporter.exportReport();
            return baos.toByteArray();
        } catch (Exception e) {
            if (e instanceof JRException) {
                throw (JRException) e;
            }
            throw new JRException("Failed to export DOCX", e);
        }
    }
}
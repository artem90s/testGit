package com.example.reportservice.service.export.impl;

import com.example.reportservice.service.export.ReportExporter;
import com.example.reportservice.service.export.ReportFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class XlsxReportExporter implements ReportExporter {
    @Override
    public ReportFormat getFormat() {
        return ReportFormat.XLSX;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String getFileExtension() {
        return "xlsx";
    }

    @Override
    public byte[] export(JasperPrint jasperPrint) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setWhitePageBackground(false);

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
            throw new JRException("Failed to export XLSX", e);
        }
    }
}
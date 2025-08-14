package com.example.reportservice.service.export.impl;

import com.example.reportservice.service.export.ReportExporter;
import com.example.reportservice.service.export.ReportFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Component;

@Component
public class PdfReportExporter implements ReportExporter {
    @Override
    public ReportFormat getFormat() {
        return ReportFormat.PDF;
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }

    @Override
    public byte[] export(JasperPrint jasperPrint) throws JRException {
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
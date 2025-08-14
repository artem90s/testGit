package com.example.reportservice.service.export;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface ReportExporter {
    ReportFormat getFormat();
    String getContentType();
    String getFileExtension();
    byte[] export(JasperPrint jasperPrint) throws JRException;
}
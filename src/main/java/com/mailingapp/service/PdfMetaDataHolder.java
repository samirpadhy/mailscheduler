package com.mailingapp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Samir
 * @since 1.0 21/03/2015
 */
public class PdfMetaDataHolder {

    private Map<String, Object> reportHeaderDetail;

    private String jasperTemplateFileName;

    private String pdfFileNameWithPath;

    private List<?> pdfData;

    public PdfMetaDataHolder(Map<String, Object> reportHeaderDetail, String jasperTemplateFileName, String pdfFileNameWithPath, List<?> pdfData) {
        this.reportHeaderDetail = reportHeaderDetail;
        this.jasperTemplateFileName = jasperTemplateFileName;
        this.pdfFileNameWithPath = pdfFileNameWithPath;
        this.pdfData = pdfData;
    }

    public Map<String, Object> getReportHeaderDetail() {
        return reportHeaderDetail;
    }

    public String getJasperTemplateFileName() {
        return jasperTemplateFileName;
    }

    public String getPdfFileNameWithPath() {
        return pdfFileNameWithPath;
    }

    public List<?> getPdfData() {
        return pdfData;
    }
}

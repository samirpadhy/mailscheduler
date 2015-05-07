package com.mailingapp.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author: Samir
 * @since 1.0 21/03/2015
 */

@Service
public class PDFGeneratorService {

    public File generatePdfFile(PdfMetaDataHolder pdfMetaDataHolder) throws IOException, JRException {
        PDFGenerator pdfGenerator = new PDFGenerator(pdfMetaDataHolder.getReportHeaderDetail(), pdfMetaDataHolder.getJasperTemplateFileName(), pdfMetaDataHolder.getPdfFileNameWithPath());
        File pdfFile = pdfGenerator.createdPDFReportByJavaList(pdfMetaDataHolder.getPdfData());
        return pdfFile;
    }

    private class PDFGenerator {

        private Map<String, Object> reportHeaderDetail;

        private File pdfFile;

        private String jasperTemplateFileName;

        private String pdfFileNameWithPath;

        public PDFGenerator(Map<String, Object> reportHeaderDetail, String jasperTemplateFileName, String pdfFileNameWithPath) {
            this.reportHeaderDetail = reportHeaderDetail;
            this.jasperTemplateFileName = jasperTemplateFileName;
            this.pdfFileNameWithPath = pdfFileNameWithPath;
            this.pdfFile = new File(pdfFileNameWithPath);
        }

        public <T> File createdPDFReportByJavaList(List<T> reportData) throws IOException, JRException {
            InputStream inputStream = PDFGeneratorService.class.getClassLoader().getResourceAsStream(jasperTemplateFileName);
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, reportHeaderDetail, new JRBeanCollectionDataSource(reportData, false));
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileNameWithPath);
            return pdfFile;
        }
    }
}

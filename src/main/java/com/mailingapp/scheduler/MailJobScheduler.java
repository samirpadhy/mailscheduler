package com.mailingapp.scheduler;

import com.mailingapp.domain.EmailAttachment;
import com.mailingapp.query.MailDataFinder;
import com.mailingapp.service.AuditLogService;
import com.mailingapp.service.MailService;
import com.mailingapp.service.PDFGeneratorService;
import com.mailingapp.service.PdfMetaDataHolder;
import net.sf.jasperreports.engine.JRException;
import org.dom4j.DocumentException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Component
public class MailJobScheduler {

    private AuditLogService auditLogService;

    private MailService mailService;

    private MailDataFinder mailDataFinder;

    private PDFGeneratorService pdfGeneratorService;


    @Autowired
    public MailJobScheduler(AuditLogService auditLogService, MailService mailService, MailDataFinder mailDataFinder, PDFGeneratorService pdfGeneratorService) {
        this.auditLogService = auditLogService;
        this.mailService = mailService;
        this.mailDataFinder = mailDataFinder;
        this.pdfGeneratorService = pdfGeneratorService;
    }


    @Scheduled(cron = "${spring.cronexpression}")
    public void schedule() throws IOException, DocumentException, JRException {
        Map<String, Object> reportHeaderDetail = new HashMap<String, Object>();
        reportHeaderDetail.put("pPracticeName", "Fortis Hospital,Bangalore");
        reportHeaderDetail.put("pReportTitle", "Account Summary");
        reportHeaderDetail.put("pReportParam", "Test");
        reportHeaderDetail.put("repColumn1", "Code");
        reportHeaderDetail.put("repColumn2", "Account Name");
        int currentWeekDay = LocalDate.now().getDayOfWeek();
        int startOfWeekInterval = (currentWeekDay - 1) * -1;
        int endOfWeekInterval = 6 - currentWeekDay;
        if (endOfWeekInterval < 0) {
            endOfWeekInterval = 0;
        }
        Date fromDate = LocalDate.now().plusDays(startOfWeekInterval).toDate();
        Date thruDate = LocalDate.now().plusDays(endOfWeekInterval).toDate();

        List<Map<String, Object>> pdfData = mailDataFinder.getBillingPaymentSummary(null, fromDate, thruDate);
        PdfMetaDataHolder pdfMetaDataHolder = new PdfMetaDataHolder(reportHeaderDetail, "reppaymentsummary.jasper", "paymentsummary.pdf", pdfData);
        File pdfFile = pdfGeneratorService.generatePdfFile(pdfMetaDataHolder);
        EmailAttachment emailAttachment = new EmailAttachment(pdfFile.getName(), pdfFile);
        List<EmailAttachment> emailAttachments = new ArrayList<EmailAttachment>();
        emailAttachments.add(emailAttachment);
        Map<String, Object> emailData = new HashMap<String, Object>();
        emailData.put("partyName", "Senthil Muthiah");
        emailData.put("fromDate", fromDate);
        emailData.put("thruDate", thruDate);
        emailData.put("recipientMailAddress", "samir_padhy@nthdimenzion.com");
        mailService.sendPaymentSummaryMail(emailAttachments, emailData);
        //auditLogService.createAudit("test@gmail.com", new Date(System.currentTimeMillis()));
        cleanTempFiles(emailAttachments);
    }

    private void cleanTempFiles(List<EmailAttachment> emailAttachments) {
        for (EmailAttachment emailAttachment : emailAttachments) {
            emailAttachment.getFile().delete();
        }
    }
}

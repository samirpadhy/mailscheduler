package com.mailingapp.service;

import com.mailingapp.domain.EmailAttachment;
import org.apache.velocity.app.VelocityEngine;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.activation.FileDataSource;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Service
public class MailService {


    private JavaMailSenderImpl mailSender;

    private VelocityEngine velocityEngine;

    @Autowired
    public MailService(JavaMailSenderImpl mailSender, VelocityEngine velocityEngine) {
        this.mailSender = mailSender;
        this.velocityEngine = velocityEngine;
    }

    public Map<String, Object> sendMailWithAttachment(final String subject, final String messageBody, final List<EmailAttachment> attachments, final String recipientMailAddress) throws DocumentException,
            IOException {
        final Map<String, Object> resultMap = new HashMap<String, Object>();
        System.out.println("Send Mail service invoked");
        try {
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    Properties properties = new Properties();
                    ClassLoader bundleClassLoader = Thread.currentThread().getContextClassLoader();
                    properties.load(bundleClassLoader.getResourceAsStream("mailsettings.properties"));
                    mailSender.setUsername(properties.getProperty("mail.user"));
                    mailSender.setPassword(properties.getProperty("mail.password"));
                    mailSender.setJavaMailProperties(properties);
                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, "UTF-8");
                    message.setTo(recipientMailAddress);
                    message.setFrom(properties.getProperty("mail.sentFrom"));
                    message.setSubject(subject);
                    message.setText(messageBody, true);
                    for (final EmailAttachment attachment : attachments) {
                        String attachmentName = attachment.getName();
                        FileDataSource dataSource = new FileDataSource(attachment.getFile()) {
                            @Override
                            public String getContentType() {
                                return attachment.getContentType();
                            }
                        };
                        message.addAttachment(attachmentName, dataSource);
                    }
                }
            };
            this.mailSender.send(preparator);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultMap;
    }


    public void sendPaymentSummaryMail(List<EmailAttachment> emailAttachments, Map<String, Object> dataMap) throws IOException, DocumentException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fromDate = simpleDateFormat.format(dataMap.get("fromDate"));
        String thruDate = simpleDateFormat.format(dataMap.get("thruDate"));
        dataMap.put("fromDate", fromDate);
        dataMap.put("thruDate", thruDate);
        String emailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/paymentsummarymailtemplate.vm", dataMap);
        sendMailWithAttachment("Payment summary", emailBody, emailAttachments, (String) dataMap.get("recipientMailAddress"));
    }
}

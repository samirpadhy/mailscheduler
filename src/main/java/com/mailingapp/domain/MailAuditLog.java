package com.mailingapp.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Entity
public class MailAuditLog {

    @Id
    @GeneratedValue
    private Long id;

    private String mailSendTo;

    private Date mailSentOn;


    MailAuditLog() {
    }

    public MailAuditLog(String mailSendTo, Date mailSentOn) {
        this.mailSendTo = mailSendTo;
        this.mailSentOn = mailSentOn;

    }
}

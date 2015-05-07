package com.mailingapp.service;

import com.mailingapp.domain.MailAuditLog;
import org.sam.application.JpaRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * @author: Samir
 * @since 1.0 06/03/2015
 */
@Service
public class AuditLogService {

    private JpaRepositoryFactory jpaRepositoryFactory;

    @Autowired
    public AuditLogService(JpaRepositoryFactory jpaRepositoryFactory) {
        this.jpaRepositoryFactory = jpaRepositoryFactory;

    }

    @Transactional
    public void createAudit(String mailSentTo, Date mailSentOn) {
        JpaRepository<MailAuditLog, Long> mailAuditLogLongJpaRepository = jpaRepositoryFactory.getRepository(MailAuditLog.class);
        MailAuditLog mailAuditLog = new MailAuditLog(mailSentTo, mailSentOn);
        mailAuditLogLongJpaRepository.save(mailAuditLog);
    }
}

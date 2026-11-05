
package kz.university.iam.service;

import kz.university.iam.entity.AuditLog;
import kz.university.iam.repository.AuditLogRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logAction(
            String username,
            String action,
            String details) {

        AuditLog auditLog = new AuditLog(
                username,
                action,
                details
        );

        auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getUserLogs(String username) {
        return auditLogRepository.findByUsername(username);
    }
}

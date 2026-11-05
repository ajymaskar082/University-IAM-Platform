
package kz.university.iam.controller;

import kz.university.iam.entity.AuditLog;
import kz.university.iam.entity.User;
import kz.university.iam.repository.UserRepository;
import kz.university.iam.service.AuditLogService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public AdminController(
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {

        List<Map<String, Object>> users = userRepository.findAll()
                .stream()
                .map(user -> Map.<String, Object>of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "enabled", user.isEnabled(),
                        "mfaEnabled", user.isMfaEnabled()
                ))
                .toList();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пайдаланушы табылмады"));

        Boolean enabled = request.get("enabled");

        if (enabled == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "enabled мәні қажет"));
        }

        user.setEnabled(enabled);
        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Пайдаланушы статусы жаңартылды")
        );
    }

    @GetMapping("/audit-logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }
}

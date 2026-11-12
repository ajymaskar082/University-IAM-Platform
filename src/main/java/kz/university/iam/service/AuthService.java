
package kz.university.iam.service;

import kz.university.iam.entity.Role;
import kz.university.iam.entity.User;
import kz.university.iam.repository.RoleRepository;
import kz.university.iam.repository.UserRepository;
import kz.university.iam.security.JwtService;
import kz.university.iam.security.TotpService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TotpService totpService;
    private final AuditLogService auditLogService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TotpService totpService,
            AuditLogService auditLogService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.totpService = totpService;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public User register(String username, String email, String password) {

        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Тіркеу деректері дұрыс емес");
        }

        if (userRepository.existsByUsername(username)
                || userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Логин немесе email тіркелген");
        }

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalStateException(
                        "STUDENT рөлі табылмады"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setMfaEnabled(false);
        user.setMfaSecret(totpService.generateSecret());
        user.getRoles().add(studentRole);

        User savedUser = userRepository.save(user);

        auditLogService.logAction(
                username, "REGISTER",
                "Жаңа пайдаланушы тіркелді");

        return savedUser;
    }

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public Map<String, String> login(
            String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Логин немесе құпиясөз қате"));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException(
                    "Аккаунт өшірілген");
        }

        if (user.getLockTime() != null) {

            if (user.getLockTime().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException(
                        "Аккаунт уақытша бұғатталған");
            }

            user.setLockTime(null);
            user.setFailedLoginAttempts(0);
        }

        if (!passwordEncoder.matches(
                password, user.getPassword())) {

            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                user.setLockTime(
                        LocalDateTime.now()
                                .plusMinutes(LOCK_MINUTES));
            }

            userRepository.save(user);

            auditLogService.logAction(
                    username,
                    "LOGIN_FAILED",
                    "Қате кіру әрекеті: " + attempts);

            throw new IllegalArgumentException(
                    "Логин немесе құпиясөз қате");
        }

        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);

        if (user.isMfaEnabled()) {

            auditLogService.logAction(
                    username,
                    "MFA_REQUIRED",
                    "Бір реттік код енгізу қажет");

            return Map.of(
                    "mfaRequired", "true",
                    "username", username
            );
        }

        auditLogService.logAction(
                username,
                "LOGIN_SUCCESS",
                "Қолданушы жүйеге кірді");

        return generateTokens(username);
    }

    @Transactional
    public Map<String, String> verifyMfa(
            String username, String code) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пайдаланушы табылмады"));

        if (!user.isEnabled() || !user.isMfaEnabled()) {
            throw new IllegalArgumentException(
                    "MFA белсенді емес");
        }

        if (!totpService.verifyCode(
                user.getMfaSecret(), code)) {

            auditLogService.logAction(
                    username,
                    "MFA_FAILED",
                    "MFA коды қате");

            throw new IllegalArgumentException(
                    "MFA коды қате");
        }

        auditLogService.logAction(
                username,
                "LOGIN_SUCCESS",
                "MFA арқылы жүйеге кіру сәтті аяқталды");

        return generateTokens(username);
    }

    @Transactional
    public String enableMfa(String username, String code) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пайдаланушы табылмады"));

        if (user.isMfaEnabled()) {
            throw new IllegalArgumentException(
                    "MFA бұрыннан қосылған");
        }

        if (!totpService.verifyCode(
                user.getMfaSecret(), code)) {
            throw new IllegalArgumentException(
                    "MFA коды қате");
        }

        user.setMfaEnabled(true);
        userRepository.save(user);

        auditLogService.logAction(
                username,
                "MFA_ENABLED",
                "MFA белсендірілді");

        return "MFA сәтті қосылды";
    }

    private Map<String, String> generateTokens(
            String username) {

        return Map.of(
                "accessToken",
                jwtService.generateAccessToken(username),

                "refreshToken",
                jwtService.generateRefreshToken(username),

                "tokenType",
                "Bearer"
        );
    }
}

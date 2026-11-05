
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

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TotpService totpService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            TotpService totpService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.totpService = totpService;
    }

    @Transactional
    public User register(String username, String email, String password) {
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.length() < 8) {
            throw new IllegalArgumentException("Тіркеу деректері дұрыс емес");
        }

        if (userRepository.existsByUsername(username)
                || userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Логин немесе email тіркелген");
        }

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalStateException("STUDENT рөлі табылмады"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setMfaEnabled(false);
        user.setMfaSecret(totpService.generateSecret());
        user.getRoles().add(studentRole);

        return userRepository.save(user);
    }

    public Map<String, String> login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Логин немесе құпиясөз қате"));

        if (!user.isEnabled()
                || !passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Логин немесе құпиясөз қате");
        }

        if (user.isMfaEnabled()) {
            throw new IllegalArgumentException("MFA коды қажет");
        }

        return generateTokens(username);
    }

    public Map<String, String> verifyMfa(String username, String code) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пайдаланушы табылмады"));

        if (!user.isEnabled() || !user.isMfaEnabled()) {
            throw new IllegalArgumentException("MFA белсенді емес");
        }

        if (!totpService.verifyCode(user.getMfaSecret(), code)) {
            throw new IllegalArgumentException("MFA коды қате");
        }

        return generateTokens(username);
    }

    @Transactional
public String enableMfa(String username, String code) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Пайдаланушы табылмады"
            ));

    if (user.isMfaEnabled()) {
        throw new IllegalArgumentException(
                "MFA бұрыннан қосылған"
        );
    }

    if (!totpService.verifyCode(user.getMfaSecret(), code)) {
        throw new IllegalArgumentException(
                "MFA коды қате"
        );
    }

    user.setMfaEnabled(true);
    userRepository.save(user);

    return "MFA сәтті қосылды";
}
    
    private Map<String, String> generateTokens(String username) {
        return Map.of(
                "accessToken", jwtService.generateAccessToken(username),
                "refreshToken", jwtService.generateRefreshToken(username),
                "tokenType", "Bearer"
        );
    }
}

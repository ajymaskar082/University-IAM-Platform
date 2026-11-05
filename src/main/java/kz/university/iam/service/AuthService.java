
package kz.university.iam.service;

import kz.university.iam.entity.Role;
import kz.university.iam.entity.User;
import kz.university.iam.repository.RoleRepository;
import kz.university.iam.repository.UserRepository;
import kz.university.iam.security.JwtService;

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

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public User register(String username, String email, String password) {

        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Барлық өрістер толтырылуы керек");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException(
                    "Құпиясөз кемінде 8 таңбадан тұруы керек");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Логин тіркелген");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email тіркелген");
        }

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalStateException(
                        "STUDENT рөлі табылмады"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(studentRole);

        return userRepository.save(user);
    }

    public Map<String, String> login(
            String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Логин немесе құпиясөз қате"));

        if (!user.isEnabled()
                || !passwordEncoder.matches(
                        password, user.getPassword())) {
            throw new IllegalArgumentException(
                    "Логин немесе құпиясөз қате");
        }

        String accessToken =
                jwtService.generateAccessToken(username);

        String refreshToken =
                jwtService.generateRefreshToken(username);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "tokenType", "Bearer"
        );
    }
}

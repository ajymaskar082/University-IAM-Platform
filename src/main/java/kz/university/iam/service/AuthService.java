
package kz.university.iam.service;

import kz.university.iam.entity.Role;
import kz.university.iam.entity.User;
import kz.university.iam.repository.RoleRepository;
import kz.university.iam.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
            throw new IllegalArgumentException(
                    "Бұл логин бұрын тіркелген");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Бұл электрондық пошта бұрын тіркелген");
        }

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalStateException(
                        "STUDENT рөлі деректер базасында табылмады"));

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add(studentRole);

        return userRepository.save(user);
    }
}


package kz.university.iam.service;

import kz.university.iam.entity.Role;
import kz.university.iam.entity.User;
import kz.university.iam.repository.RoleRepository;
import kz.university.iam.repository.UserRepository;
import kz.university.iam.security.JwtService;
import kz.university.iam.security.TotpService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAttemptTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TotpService totpService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("student");
        user.setPassword("hashedPassword");
        user.setEnabled(true);
        user.setFailedLoginAttempts(0);
    }

    @Test
    void shouldLockAccountAfterFiveFailedAttempts() {

        when(userRepository.findByUsername("student"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "hashedPassword"))
                .thenReturn(false);

        for (int i = 0; i < 5; i++) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> authService.login("student", "wrong")
            );
        }

        assertEquals(5, user.getFailedLoginAttempts());
        assertNotNull(user.getLockTime());
        assertTrue(user.getLockTime().isAfter(LocalDateTime.now()));

        verify(userRepository, times(5)).save(user);
    }

    @Test
    void shouldRejectLoginWhenAccountIsLocked() {

        user.setFailedLoginAttempts(5);
        user.setLockTime(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findByUsername("student"))
                .thenReturn(Optional.of(user));

        assertThrows(
                IllegalArgumentException.class,
                () -> authService.login("student", "correct")
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());
    }

    @Test
    void shouldResetFailedAttemptsAfterSuccessfulPassword() {

        user.setFailedLoginAttempts(3);

        when(userRepository.findByUsername("student"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("correct", "hashedPassword"))
                .thenReturn(true);

        when(jwtService.generateAccessToken("student"))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken("student"))
                .thenReturn("refresh-token");

        authService.login("student", "correct");

        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLockTime());
    }
}

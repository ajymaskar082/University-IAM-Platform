
package kz.university.iam.controller;

import kz.university.iam.entity.User;
import kz.university.iam.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> request) {
        try {
            User user = authService.register(
                    request.get("username"),
                    request.get("email"),
                    request.get("password")
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Тіркелу сәтті аяқталды",
                            "username", user.getUsername(),
                            "mfaSecret", user.getMfaSecret()
                    ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(
                    authService.login(
                            request.get("username"),
                            request.get("password")
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<?> verifyMfa(
            @RequestBody Map<String, String> request) {
        try {
            return ResponseEntity.ok(
                    authService.verifyMfa(
                            request.get("username"),
                            request.get("code")
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}


classDiagram
    class User {
        +Long id
        +String username
        +String email
        +String password
        +boolean mfaEnabled
        +boolean enabled 
        +register()
        +updateProfile()
    } 

    class Role {
        +Long id
        +String name
    }

    class Permission {
        +Long id
        +String name
    }

    class AuditLog {
        +Long id
        +String action
        +String username
        +LocalDateTime timestamp
        +String ipAddress
    }

    class LoginAttempt {
        +Long id
        +String username
        +int attempts
        +LocalDateTime lastAttempt
    }

    class AuthService {
        +register()
        +login()
        +verifyPassword()
    }

    class JwtService {
        +generateAccessToken()
        +generateRefreshToken()
        +validateToken()
    }

    class MfaService {
        +generateSecret()
        +verifyCode()
    }

    class AccessControlService {
        +checkRole()
        +checkPermission()
        +checkAttributes()
    }

    class UserRepository {
        +findByUsername()
        +save()
        +findById()
    }

    class AuthController {
        +register()
        +login()
        +verifyMfa()
    }

    User "*" -- "*" Role : has
    Role "*" -- "*" Permission : grants
    User "1" --> "*" AuditLog : generates
    User "1" --> "*" LoginAttempt : has

    AuthController --> AuthService
    AuthService --> UserRepository
    AuthService --> JwtService
    AuthService --> MfaService
    AccessControlService --> UserRepository
    AccessControlService --> Role

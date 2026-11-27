
flowchart TB
    subgraph CLIENT["Клиенттік деңгей"]
        UI["Thymeleaf Web Interface"]
        USER["Студент / Оқытушы / Әкімші"]
    end

    subgraph BACKEND["Серверлік деңгей — Spring Boot"]
        SEC["Spring Security"]
        AUTH["Authentication Service"]
        JWT["JWT Service"]
        MFA["MFA / TOTP Service"]
        RBAC["RBAC / ABAC Service"]
        AUDIT["Audit Log Service"]
        ADMIN["Admin Panel"]
    end

    subgraph DATA["Деректер деңгейі"]
        DB[("PostgreSQL")]
        USERS["Users"]
        ROLES["Roles / Permissions"]
        LOGS["Audit Logs"]
        ATTEMPTS["Login Attempts"]
    end

    USER --> UI
    UI --> SEC
    SEC --> AUTH
    AUTH --> JWT
    AUTH --> MFA
    SEC --> RBAC
    AUTH --> AUDIT
    RBAC --> ADMIN
    AUTH --> DB
    RBAC --> DB
    AUDIT --> DB
    DB --> USERS
    DB --> ROLES
    DB --> LOGS
    DB --> ATTEMPTS


subgraph BACKEND["Серверлік деңгей — Spring Boot"]
    SEC["Spring Security"]
    AUTH["Authentication Service"]
    MFA["MFA / TOTP Service"]
    JWT["JWT Service"]
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
AUTH --> MFA
MFA --> JWT
JWT --> RBAC
RBAC --> ADMIN

AUTH --> AUDIT
SEC --> AUDIT

AUTH --> DB
MFA --> DB
JWT --> DB
RBAC --> DB
AUDIT --> DB

DB --- USERS
DB --- ROLES
DB --- LOGS
DB --- ATTEMPTS

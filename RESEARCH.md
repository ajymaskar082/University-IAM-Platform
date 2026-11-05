# ЗЕРТТЕУ ЖҰМЫСЫ (RESEARCH)
# University IAM Platform

## 1. Кіріспе

Қазіргі уақытта университеттерде студенттердің, оқытушылардың және әкімшілік қызметкерлердің жеке ақпараттары әртүрлі ақпараттық жүйелерде сақталады. Осы жүйелерге қауіпсіз кіруді қамтамасыз ету және пайдаланушылардың құқықтарын басқару маңызды міндеттердің бірі болып табылады.

Осы мәселені шешу үшін Identity and Access Management (IAM) платформасын әзірлеу ұсынылады.

## 2. IAM жүйесі дегеніміз не?

IAM (Identity and Access Management) – пайдаланушылардың жеке басын сәйкестендіруді және ақпараттық ресурстарға қол жеткізу құқықтарын басқаруды қамтамасыз ететін жүйе.

IAM жүйесінің негізгі міндеттері:
- Пайдаланушыларды тіркеу.
- Жеке басын сәйкестендіру.
- Рөлдер мен құқықтарды басқару.
- Жүйеге кіру қауіпсіздігін қамтамасыз ету.
- Пайдаланушылардың әрекеттерін бақылау.

## 3. Негізгі қауіпсіздік технологияларын зерттеу

### 3.1. Аутентификация (Authentication)

Аутентификация – пайдаланушының кім екенін тексеру процесі. Мысалы, пайдаланушы жүйеге логин мен құпиясөз арқылы кіреді.

### 3.2. Авторизация (Authorization)

Авторизация – жүйеге кірген пайдаланушының қандай әрекеттерді орындауға құқығы бар екенін анықтау процесі.

### 3.3. BCrypt

BCrypt – құпиясөздерді қауіпсіз хештеуге арналған алгоритм. Ол құпиясөздерді ашық мәтін түрінде сақтаудың орнына хеш түрінде сақтауға мүмкіндік береді.

### 3.4. JWT (JSON Web Token)

JWT – пайдаланушының жүйеге сәтті кіргенін растайтын және оның құқықтары туралы ақпаратты тасымалдайтын токен форматы.

Жүйеде Access Token және Refresh Token қолданылады.

### 3.5. MFA (Multi-Factor Authentication)

MFA – пайдаланушыны бірнеше фактор арқылы тексеру әдісі.

Біздің жобада TOTP технологиясы қолданылады. Ол уақытқа байланысты өзгеретін бір реттік кодтарды қалыптастырады.

### 3.6. RBAC (Role-Based Access Control)

RBAC – пайдаланушыларға олардың рөліне байланысты құқық беру механизмі.

Жүйедегі негізгі рөлдер:
- ADMIN – әкімші.
- TEACHER – оқытушы.
- STUDENT – студент.

### 3.7. ABAC (Attribute-Based Access Control)

ABAC – пайдаланушының атрибуттарына, құрылғысына, уақытына және басқа шарттарға байланысты қолжетімділікті басқару әдісі.

## 4. Қолданылатын технологияларды таңдау

| Технология | Таңдау себебі |
|---|---|
| Java 17+ | Негізгі бағдарламалау тілі |
| Spring Boot | Серверлік қосымша әзірлеу |
| Spring Security | Қауіпсіздік механизмдері |
| PostgreSQL | Деректерді сақтау |
| Thymeleaf | Веб-интерфейс құру |
| BCrypt | Құпиясөздерді хештеу |
| JWT | Токен арқылы аутентификация |
| TOTP | Екі факторлы аутентификация |
| JUnit 5 | Бағдарламаны тестілеу |

## 5. Жүйенің архитектурасы

Жүйе бірнеше негізгі модульден тұрады:

1. Authentication Module – пайдаланушыларды сәйкестендіру.
2. Authorization Module – рөлдер мен құқықтарды басқару.
3. User Management Module – пайдаланушыларды басқару.
4. Audit Module – әрекеттер тарихын тіркеу.
5. Web Interface – пайдаланушы интерфейсі.
6. Database – деректерді сақтау.

## 6. Зерттеу қорытындысы

Зерттеу нәтижесінде университетке арналған IAM платформасын әзірлеу үшін Spring Boot және Spring Security технологияларын қолдану тиімді екені анықталды.

BCrypt, JWT, TOTP, RBAC және ABAC механизмдері жүйенің қауіпсіздігін қамтамасыз етуге көмектеседі.

Әзірленетін жүйе университеттегі пайдаланушыларды басқаруды жеңілдетіп, ақпараттық ресурстарға қолжетімділікті орталықтандыруға бағытталған.

## 7. Пайдаланылған дереккөздер

1. Spring Security Documentation – https://docs.spring.io/spring-security/reference/
2. OWASP Password Storage Cheat Sheet – https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
3. RFC 7519 – JSON Web Token – https://www.rfc-editor.org/rfc/rfc7519.html
4. RFC 6238 – TOTP – https://www.rfc-editor.org/rfc/rfc6238.html


## 9. Security Analysis and Risk Assessment

### 9.1 Main Security Risks

The University IAM Platform may face several security risks:

- Brute-force attacks: attackers may repeatedly try different passwords.
- Password theft: weak or exposed passwords can compromise user accounts.
- Token theft: stolen JWT tokens may allow unauthorized access.
- Unauthorized access: users may attempt to access resources without sufficient permissions.
- MFA bypass: attackers may try to bypass the second authentication factor.
- Insider threats: users with excessive privileges may misuse their access.

### 9.2 Security Measures

The proposed system uses several security mechanisms:

| Security Risk | Protection Mechanism |
|---|---|
| Password theft | BCrypt password hashing |
| Brute-force attacks | Login attempt limiting |
| Unauthorized access | RBAC |
| Context-based access risks | ABAC |
| Token manipulation | JWT signature validation |
| Account compromise | TOTP-based MFA |
| Lack of accountability | Audit logging |

### 9.3 Limitations

The current implementation is a prototype. Further improvements are required:

- Encrypting MFA secrets in the database.
- Implementing refresh token rotation and revocation.
- Adding IP-based rate limiting.
- Improving audit log integrity.
- Adding automated security tests.
- Implementing a complete administrative web interface.

## 10. Conclusion

The University IAM Platform provides a centralized approach to identity and access management in a university environment.

The proposed architecture combines authentication, authorization, multi-factor authentication, account protection, and audit logging.

Spring Boot and Spring Security provide the application framework, PostgreSQL stores user information, BCrypt protects passwords, JWT supports token-based authentication, and TOTP adds an additional authentication factor.

The project demonstrates how modern security mechanisms can be combined to improve the protection of university information systems.

## 11. References

1. Spring Security Documentation: https://docs.spring.io/spring-security/reference/
2. OWASP Password Storage Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
3. RFC 7519 - JSON Web Token: https://www.rfc-editor.org/rfc/rfc7519
4. RFC 6238 - TOTP: https://www.rfc-editor.org/rfc/rfc6238
5. OWASP Authentication Cheat Sheet: https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html
6. PostgreSQL Documentation: https://www.postgresql.org/docs/

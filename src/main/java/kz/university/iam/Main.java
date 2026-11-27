package kz.university.iam;

import kz.university.iam.concurrency.LoginAttemptLimiter;
import kz.university.iam.concurrency.LoginSimulator;
import kz.university.iam.dao.AuditLogDao;
import kz.university.iam.dao.DatabaseManager;
import kz.university.iam.dao.JdbcUserDao;
import kz.university.iam.dao.UserDao;
import kz.university.iam.export.ReportExporter;
import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Role;
import kz.university.iam.model.User;
import kz.university.iam.model.UserFactory;
import kz.university.iam.repository.InMemoryRepository;
import kz.university.iam.repository.Repository;
import kz.university.iam.security.PasswordHasher;
import kz.university.iam.service.AnalyticsService;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * University IAM Platform — демонстрациялық бағдарлама.
 * Әр талап үшін код бөлімдері комментарийлермен белгіленген.
 * [ТАЛАП: ...] БАСТАЛАДЫ  ...  [ТАЛАП: ...] АЯҚТАЛДЫ
 */
public class Main {

    public static void main(String[] args) throws Exception {
        // Windows консолінде қазақ әріптері дұрыс шығуы үшін UTF-8 қолданамыз
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Path dbFile = Path.of("iam_demo.db");
        Files.deleteIfExists(dbFile);

        // >>> [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] БАСТАЛАДЫ — SQLite + JDBC + DAO
        title("1. ДЕРЕКТЕР ҚОРЫ (SQLite + JDBC + DAO)");
        DatabaseManager db = DatabaseManager.forFile(dbFile.toString());
        db.initSchema();
        UserDao userDao = new JdbcUserDao(db);
        AuditLogDao auditDao = new AuditLogDao(db);

        userDao.insert(newUser(Role.ADMIN, "admin", "Асылхан Ермеков", "Әкімшілік", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.TEACHER, "teacher1", "Гүлнар Сәрсенова", "Информатика", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.TEACHER, "teacher2", "Марат Омаров", "Математика", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.STUDENT, "student1", "Айгерім Нұрланқызы", "Информатика", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.STUDENT, "student2", "Данияр Қайратұлы", "Информатика", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.STUDENT, "student3", "Алуа Төлеубай", "Математика", AccountStatus.ACTIVE));
        userDao.insert(newUser(Role.STUDENT, "student4", "Ербол Серікұлы", "Математика", AccountStatus.GRADUATED));

        List<User> users = userDao.findAll();
        System.out.println("Деректер қорына қосылған пайдаланушылар саны: " + users.size());
        // <<< [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] АЯҚТАЛДЫ (транзакция 6-бөлімде көрсетіледі)

        // >>> [ТАЛАП: ООП] БАСТАЛАДЫ — ПОЛИМОРФИЗМ демонстрациясы
        title("2. ООП: ПОЛИМОРФИЗМ, МҰРАГЕРЛІК, ИНТЕРФЕЙС, ENUM");
        for (User u : users) {
            // Бір ғана User типі, бірақ getProfileInfo() және canAccess() әр рөлде әртүрлі жұмыс істейді
            System.out.println(u.getProfileInfo()
                    + " | бағаларға рұқсат: " + (u.canAccess("grades") ? "иә" : "жоқ")
                    + " | күйі: " + u.getStatus().getTitle());
        }
        // <<< [ТАЛАП: ООП] АЯҚТАЛДЫ

        // >>> [ТАЛАП: КОЛЛЕКЦИЯЛАР ЖӘНЕ GENERICS] БАСТАЛАДЫ — List, Set, Map, Repository<T, ID>
        title("3. КОЛЛЕКЦИЯЛАР ЖӘНЕ GENERICS");
        Repository<User, Integer> repository = new InMemoryRepository<>(User::getId); // Generic класс
        users.forEach(repository::save);                                              // List -> Repository (Map)
        System.out.println("Репозиторийдегі жазбалар саны: " + repository.count());
        repository.findById(4).ifPresent(u -> System.out.println("ID=4 табылды: " + u));
        System.out.println("ID=99 бар ма: " + repository.findById(99).isPresent());
        // <<< [ТАЛАП: КОЛЛЕКЦИЯЛАР ЖӘНЕ GENERICS] АЯҚТАЛДЫ

        // >>> [ТАЛАП: STREAM API] БАСТАЛАДЫ — filter, map, groupingBy
        title("4. STREAM API: ЕСЕПТЕР МЕН АНАЛИТИКА");
        AnalyticsService analytics = new AnalyticsService(repository.findAll());
        System.out.println("Белсенді аккаунттар: " + analytics.activeUsers().size());
        System.out.println("Студенттер (filter + map): " + analytics.fullNamesByRole(Role.STUDENT));
        System.out.println("Рөл бойынша (groupingBy): " + analytics.countByRole());
        System.out.println("Факультет бойынша (groupingBy): " + analytics.countByFaculty());
        Set<String> faculties = analytics.faculties(); // Set — қайталанбайтын мәндер
        System.out.println("Факультеттер (Set): " + faculties);
        Map<AccountStatus, List<String>> byStatus = analytics.usernamesByStatus();
        System.out.println("Мәртебе бойынша логиндер (Map): " + byStatus);
        // <<< [ТАЛАП: STREAM API] АЯҚТАЛДЫ

        // >>> [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] БАСТАЛАДЫ — ТРАНЗАКЦИЯ: бұғаттау + аудит
        title("5. ТРАНЗАКЦИЯ: пайдаланушыны бұғаттау және аудит жазбасы");
        int studentId = userDao.findByUsername("student1").orElseThrow().getId();
        boolean blocked = userDao.blockUserWithAudit(studentId, "Қауіпсіздік саясатын бұзу");
        System.out.println("Бұғатталды: " + blocked);
        System.out.println("Жаңа күйі: " + userDao.findById(studentId).orElseThrow().getStatus().getTitle());
        System.out.println("Аудит журналы: " + auditDao.findActionsByUser(studentId));
        // <<< [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] АЯҚТАЛДЫ — ТРАНЗАКЦИЯ

        // >>> [ТАЛАП: ФАЙЛДАР] БАСТАЛАДЫ — CSV және JSON экспорты
        title("6. ФАЙЛДАР: ЕСЕПТІ CSV/JSON ФАЙЛЫНА ЭКСПОРТТАУ");
        ReportExporter exporter = new ReportExporter();
        List<User> latest = userDao.findAll();
        Path csv = Path.of("reports", "users.csv");
        Path json = Path.of("reports", "users.json");
        exporter.exportToCsv(latest, csv);
        exporter.exportToJson(latest, json);
        System.out.println("CSV файлы жасалды: " + csv.toAbsolutePath());
        System.out.println("JSON файлы жасалды: " + json.toAbsolutePath());
        // <<< [ТАЛАП: ФАЙЛДАР] АЯҚТАЛДЫ

        // >>> [ТАЛАП: КӨПАҒЫНДЫЛЫҚ] БАСТАЛАДЫ — ExecutorService, AtomicInteger, ConcurrentHashMap
        title("7. КӨПАҒЫНДЫЛЫҚ: бір мезгілде қате кіру әрекеттері");
        LoginAttemptLimiter limiter = new LoginAttemptLimiter(5);
        int total = LoginSimulator.simulate(limiter, "student2", 10, 5); // 10 ағын x 5 әрекет
        System.out.println("Күтілетін қате әрекет саны: 50, нақты саны: " + total);
        System.out.println("student2 аккаунты бұғатталуы керек пе: " + limiter.isLocked("student2"));
        // <<< [ТАЛАП: КӨПАҒЫНДЫЛЫҚ] АЯҚТАЛДЫ

        System.out.println();
        System.out.println("Бағдарлама сәтті аяқталды.");
    }

    private static User newUser(Role role, String username, String fullName,
                                String faculty, AccountStatus status) {
        String hash = PasswordHasher.hash("Qwerty123!");
        return UserFactory.create(role, 0, username, fullName, faculty, hash, status);
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}

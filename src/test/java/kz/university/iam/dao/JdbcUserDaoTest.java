package kz.university.iam.dao;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Role;
import kz.university.iam.model.Student;
import kz.university.iam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// >>> [ТАЛАП: ТЕСТІЛЕУ] 4-модуль: dao (SQLite + JDBC + транзакция) — 4 тест
class JdbcUserDaoTest {

    private UserDao dao;
    private AuditLogDao audit;

    @BeforeEach
    void setUp(@TempDir Path dir) {
        DatabaseManager db = DatabaseManager.forFile(dir.resolve("test.db").toString());
        db.initSchema();
        dao = new JdbcUserDao(db);
        audit = new AuditLogDao(db);
    }

    private User newStudent(String username) {
        return new Student(0, username, "Аты-жөні", "IT", "hash", AccountStatus.ACTIVE);
    }

    @Test
    void insertAndFindByUsername() {
        int id = dao.insert(newStudent("student1"));
        User found = dao.findByUsername("student1").orElseThrow();
        assertEquals(id, found.getId());
        assertEquals(Role.STUDENT, found.getRole());
    }

    @Test
    void updateStatusChangesValue() {
        int id = dao.insert(newStudent("student1"));
        assertTrue(dao.updateStatus(id, AccountStatus.GRADUATED));
        assertEquals(AccountStatus.GRADUATED, dao.findById(id).orElseThrow().getStatus());
    }

    @Test
    void transactionCommitsStatusAndAuditTogether() {
        int id = dao.insert(newStudent("student1"));
        assertTrue(dao.blockUserWithAudit(id, "тест себебі"));
        assertEquals(AccountStatus.BLOCKED, dao.findById(id).orElseThrow().getStatus());
        assertEquals(1, audit.findActionsByUser(id).size());
    }

    @Test
    void transactionForUnknownUserWritesNothing() {
        assertFalse(dao.blockUserWithAudit(999, "жоқ пайдаланушы"));
        assertEquals(0, audit.count());
    }
}
// <<< [ТАЛАП: ТЕСТІЛЕУ] dao модулі аяқталды

package kz.university.iam.model;

import kz.university.iam.security.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// >>> [ТАЛАП: ТЕСТІЛЕУ] 1-модуль: model (ООП) — 4 тест
class UserModelTest {

    private User student() {
        return new Student(1, "student1", "Айгерім", "Информатика", "h", AccountStatus.ACTIVE);
    }

    @Test
    void studentCanOpenProfileButNotGrades() {
        User s = student();
        assertTrue(s.canAccess("profile"));
        assertFalse(s.canAccess("grades"));
    }

    @Test
    void adminCanAccessAnyResource() {
        User a = new Admin(2, "admin", "Асылхан", "Әкімшілік", "h", AccountStatus.ACTIVE);
        assertTrue(a.canAccess("grades"));
        assertTrue(a.canAccess("audit-log"));
        assertEquals(Role.ADMIN, a.getRole());
    }

    @Test
    void blockedUserHasNoAccess() {
        User t = new Teacher(3, "teacher1", "Гүлнар", "Информатика", "h", AccountStatus.ACTIVE);
        assertTrue(t.canAccess("grades"));
        t.setStatus(AccountStatus.BLOCKED);
        assertFalse(t.canAccess("grades"));
    }

    @Test
    void passwordHashIsVerifiedAndSalted() {
        String h1 = PasswordHasher.hash("Qwerty123!");
        String h2 = PasswordHasher.hash("Qwerty123!");
        assertTrue(PasswordHasher.verify("Qwerty123!", h1));
        assertFalse(PasswordHasher.verify("wrong", h1));
        assertNotEquals(h1, h2);
    }
}
// <<< [ТАЛАП: ТЕСТІЛЕУ] model модулі аяқталды

package kz.university.iam.service;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Admin;
import kz.university.iam.model.Role;
import kz.university.iam.model.Student;
import kz.university.iam.model.Teacher;
import kz.university.iam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// >>> [ТАЛАП: ТЕСТІЛЕУ] 3-модуль: service (Stream API) — 3 тест
class AnalyticsServiceTest {

    private AnalyticsService service;

    @BeforeEach
    void setUp() {
        List<User> users = List.of(
                new Admin(1, "admin", "Асылхан", "Әкімшілік", "h", AccountStatus.ACTIVE),
                new Teacher(2, "t1", "Гүлнар", "IT", "h", AccountStatus.ACTIVE),
                new Student(3, "s1", "Бота", "IT", "h", AccountStatus.ACTIVE),
                new Student(4, "s2", "Алма", "Math", "h", AccountStatus.BLOCKED));
        service = new AnalyticsService(users);
    }

    @Test
    void activeUsersExcludeBlocked() {
        assertEquals(3, service.activeUsers().size());
    }

    @Test
    void countByRoleGroupsCorrectly() {
        assertEquals(2L, service.countByRole().get(Role.STUDENT));
        assertEquals(1L, service.countByRole().get(Role.TEACHER));
    }

    @Test
    void fullNamesAreFilteredAndSorted() {
        assertEquals(List.of("Алма", "Бота"), service.fullNamesByRole(Role.STUDENT));
        assertTrue(service.faculties().contains("Math"));
        assertEquals(2L, service.countByFaculty().get("IT"));
    }
}
// <<< [ТАЛАП: ТЕСТІЛЕУ] service модулі аяқталды

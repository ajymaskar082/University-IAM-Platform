package kz.university.iam.repository;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Student;
import kz.university.iam.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// >>> [ТАЛАП: ТЕСТІЛЕУ] 2-модуль: repository (Generics) — 3 тест
class InMemoryRepositoryTest {

    private Repository<User, Integer> repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRepository<>(User::getId);
    }

    private User user(int id, String username) {
        return new Student(id, username, "Аты-жөні", "IT", "h", AccountStatus.ACTIVE);
    }

    @Test
    void savedItemCanBeFoundById() {
        repo.save(user(1, "a"));
        assertTrue(repo.findById(1).isPresent());
        assertFalse(repo.findById(2).isPresent());
    }

    @Test
    void deleteRemovesItem() {
        repo.save(user(1, "a"));
        assertTrue(repo.deleteById(1));
        assertFalse(repo.deleteById(1));
        assertEquals(0, repo.count());
    }

    @Test
    void savingSameIdReplacesItem() {
        repo.save(user(1, "old"));
        repo.save(user(1, "new"));
        assertEquals(1, repo.count());
        assertEquals("new", repo.findById(1).orElseThrow().getUsername());
        assertEquals(1, repo.findAll().size());
    }
}
// <<< [ТАЛАП: ТЕСТІЛЕУ] repository модулі аяқталды

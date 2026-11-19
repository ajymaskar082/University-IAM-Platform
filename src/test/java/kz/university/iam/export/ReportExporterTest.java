package kz.university.iam.export;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Student;
import kz.university.iam.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// >>> [ТАЛАП: ТЕСТІЛЕУ] 5-модуль: export (Файлдар) — 3 тест
class ReportExporterTest {

    private final ReportExporter exporter = new ReportExporter();

    private List<User> sample() {
        return List.of(
                new Student(1, "s1", "Айгерім", "IT", "h", AccountStatus.ACTIVE),
                new Student(2, "s2", "Тестов, Аман", "IT", "h", AccountStatus.BLOCKED));
    }

    @Test
    void csvHasHeaderAndOneLinePerUser(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("users.csv");
        exporter.exportToCsv(sample(), file);
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("username"));
    }

    @Test
    void csvEscapesCommaInsideValue(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("users.csv");
        exporter.exportToCsv(sample(), file);
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertTrue(content.contains("\"Тестов, Аман\""));
    }

    @Test
    void jsonContainsKazakhTextAndStatus(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("sub").resolve("users.json");
        exporter.exportToJson(sample(), file);
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertTrue(content.contains("Айгерім"));
        assertTrue(content.contains("BLOCKED"));
    }
}
// <<< [ТАЛАП: ТЕСТІЛЕУ] export модулі аяқталды

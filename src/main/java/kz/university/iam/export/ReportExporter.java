package kz.university.iam.export;

import kz.university.iam.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// >>> [ТАЛАП: ФАЙЛДАР] БАСТАЛАДЫ — есепті CSV және JSON файлына экспорттау
public class ReportExporter {

    /** CSV: Excel-де қазақ әріптері дұрыс көрінуі үшін басына BOM қосылады. */
    public void exportToCsv(List<User> users, Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("id,username,full_name,faculty,role,status");
        for (User u : users) {
            lines.add(String.join(",",
                    String.valueOf(u.getId()),
                    csv(u.getUsername()),
                    csv(u.getFullName()),
                    csv(u.getFaculty()),
                    u.getRole().name(),
                    u.getStatus().name()));
        }
        createParent(file);
        String content = "\uFEFF" + String.join(System.lineSeparator(), lines) + System.lineSeparator();
        Files.writeString(file, content, StandardCharsets.UTF_8);
    }

    public void exportToJson(List<User> users, Path file) throws IOException {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            sb.append("  {")
                    .append("\"id\": ").append(u.getId()).append(", ")
                    .append("\"username\": \"").append(json(u.getUsername())).append("\", ")
                    .append("\"fullName\": \"").append(json(u.getFullName())).append("\", ")
                    .append("\"faculty\": \"").append(json(u.getFaculty())).append("\", ")
                    .append("\"role\": \"").append(u.getRole().name()).append("\", ")
                    .append("\"status\": \"").append(u.getStatus().name()).append("\"")
                    .append("}");
            sb.append(i < users.size() - 1 ? ",\n" : "\n");
        }
        sb.append("]\n");
        createParent(file);
        Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
    }

    private void createParent(Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private String csv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String json(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
// <<< [ТАЛАП: ФАЙЛДАР] АЯҚТАЛДЫ

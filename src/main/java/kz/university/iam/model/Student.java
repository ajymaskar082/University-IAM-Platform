package kz.university.iam.model;

import java.util.Set;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — МҰРАГЕРЛІК (Student extends User) және ПОЛИМОРФИЗМ (@Override)
public class Student extends User {

    private static final Set<String> RESOURCES = Set.of("profile", "courses", "schedule");

    public Student(int id, String username, String fullName, String faculty,
                   String passwordHash, AccountStatus status) {
        super(id, username, fullName, faculty, passwordHash, status);
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

    @Override
    public String getProfileInfo() {
        return "Студент: " + getFullName() + ", факультет: " + getFaculty();
    }

    @Override
    protected boolean hasPermission(String resource) {
        return RESOURCES.contains(resource);
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — МҰРАГЕРЛІК: Student

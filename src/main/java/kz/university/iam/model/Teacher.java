package kz.university.iam.model;

import java.util.Set;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — МҰРАГЕРЛІК (Teacher extends User) және ПОЛИМОРФИЗМ (@Override)
public class Teacher extends User {

    private static final Set<String> RESOURCES = Set.of("profile", "courses", "schedule", "grades");

    public Teacher(int id, String username, String fullName, String faculty,
                   String passwordHash, AccountStatus status) {
        super(id, username, fullName, faculty, passwordHash, status);
    }

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }

    @Override
    public String getProfileInfo() {
        return "Оқытушы: " + getFullName() + ", кафедра: " + getFaculty();
    }

    @Override
    protected boolean hasPermission(String resource) {
        return RESOURCES.contains(resource);
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — МҰРАГЕРЛІК: Teacher

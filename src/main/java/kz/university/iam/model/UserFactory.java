package kz.university.iam.model;

/**
 * Рөлге қарай дұрыс класс нысанын жасайтын фабрика.
 */
public final class UserFactory {

    private UserFactory() {
    }

    public static User create(Role role, int id, String username, String fullName,
                              String faculty, String passwordHash, AccountStatus status) {
        return switch (role) {
            case ADMIN -> new Admin(id, username, fullName, faculty, passwordHash, status);
            case TEACHER -> new Teacher(id, username, fullName, faculty, passwordHash, status);
            case STUDENT -> new Student(id, username, fullName, faculty, passwordHash, status);
        };
    }
}

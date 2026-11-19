package kz.university.iam.model;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — ENUM (санамалы тип): Role
/**
 * Пайдаланушы рөлдері. Әр рөлдің атауы мен қолжетімділік деңгейі бар.
 */
public enum Role {
    ADMIN("Жүйе әкімшісі", 3),
    TEACHER("Оқытушы", 2),
    STUDENT("Студент", 1);

    private final String title;
    private final int accessLevel;

    Role(String title, int accessLevel) {
        this.title = title;
        this.accessLevel = accessLevel;
    }

    public String getTitle() {
        return title;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — ENUM: Role

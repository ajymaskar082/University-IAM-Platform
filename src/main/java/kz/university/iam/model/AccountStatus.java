package kz.university.iam.model;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — ENUM (санамалы тип): AccountStatus
/**
 * Аккаунттың өмірлік циклі күйлері.
 */
public enum AccountStatus {
    ACTIVE("Белсенді"),
    BLOCKED("Бұғатталған"),
    GRADUATED("Оқуын бітірген");

    private final String title;

    AccountStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — ENUM: AccountStatus

package kz.university.iam.model;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — АБСТРАКТ КЛАСС: User (интерфейсті іске асырады)
/**
 * Барлық пайдаланушылардың ортақ базалық класы.
 * Абстракт: оның өзінен нысан жасалмайды, тек Student, Teacher, Admin жасалады.
 */
public abstract class User implements Authenticatable {

    private int id;
    private final String username;
    private final String fullName;
    private final String faculty;
    private final String passwordHash;
    private AccountStatus status;

    protected User(int id, String username, String fullName, String faculty,
                   String passwordHash, AccountStatus status) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.faculty = faculty;
        this.passwordHash = passwordHash;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    // Абстракт әдістер: әр мұрагер класс өзінше іске асырады (ПОЛИМОРФИЗМ)
    public abstract Role getRole();

    public abstract String getProfileInfo();

    protected abstract boolean hasPermission(String resource);

    /**
     * Ортақ ереже: аккаунт белсенді болмаса, ешқандай ресурсқа рұқсат жоқ.
     * Содан кейін рөлге тән тексеру (hasPermission) шақырылады.
     */
    @Override
    public final boolean canAccess(String resource) {
        if (status != AccountStatus.ACTIVE) {
            return false;
        }
        return hasPermission(resource);
    }

    @Override
    public String toString() {
        return getRole().getTitle() + " [" + username + ", " + fullName + ", " + status.getTitle() + "]";
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — АБСТРАКТ КЛАСС: User

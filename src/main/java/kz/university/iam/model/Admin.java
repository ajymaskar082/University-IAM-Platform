package kz.university.iam.model;

// >>> [ТАЛАП: ООП] БАСТАЛАДЫ — МҰРАГЕРЛІК (Admin extends User) және ПОЛИМОРФИЗМ (@Override)
public class Admin extends User {

    public Admin(int id, String username, String fullName, String faculty,
                 String passwordHash, AccountStatus status) {
        super(id, username, fullName, faculty, passwordHash, status);
    }

    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public String getProfileInfo() {
        return "Әкімші: " + getFullName() + ", бөлім: " + getFaculty();
    }

    @Override
    protected boolean hasPermission(String resource) {
        // Әкімшіге барлық ресурсқа рұқсат
        return true;
    }
}
// <<< [ТАЛАП: ООП] АЯҚТАЛДЫ — МҰРАГЕРЛІК: Admin

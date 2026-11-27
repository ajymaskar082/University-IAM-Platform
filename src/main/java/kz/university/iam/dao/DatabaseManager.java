package kz.university.iam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// >>> [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] БАСТАЛАДЫ — SQLite + JDBC қосылымы және кестелер
public class DatabaseManager {

    private final String url;

    public DatabaseManager(String url) {
        this.url = url;
    }

    public static DatabaseManager forFile(String path) {
        return new DatabaseManager("jdbc:sqlite:" + path);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /** Қажетті кестелерді жасайды (бар болса, қайта жасамайды). */
    public void initSchema() {
        String users = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    full_name TEXT NOT NULL,
                    faculty TEXT NOT NULL,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL,
                    status TEXT NOT NULL
                )""";
        String audit = """
                CREATE TABLE IF NOT EXISTS audit_log (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    action TEXT NOT NULL,
                    details TEXT,
                    created_at TEXT NOT NULL
                )""";
        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            st.execute(users);
            st.execute(audit);
        } catch (SQLException e) {
            throw new DaoException("Кестелерді жасау мүмкін болмады", e);
        }
    }
}

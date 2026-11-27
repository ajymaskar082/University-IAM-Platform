package kz.university.iam.dao;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Role;
import kz.university.iam.model.User;
import kz.university.iam.model.UserFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** UserDao интерфейсінің JDBC (SQLite) арқылы іске асырылуы. */
public class JdbcUserDao implements UserDao {

    private final DatabaseManager db;
    private final AuditLogDao auditLogDao;

    public JdbcUserDao(DatabaseManager db) {
        this.db = db;
        this.auditLogDao = new AuditLogDao(db);
    }

    @Override
    public int insert(User user) {
        String sql = "INSERT INTO users(username, full_name, faculty, password_hash, role, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getFaculty());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getRole().name());
            ps.setString(6, user.getStatus().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setId(id);
                    return id;
                }
            }
            throw new DaoException("Жаңа ID алынбады", null);
        } catch (SQLException e) {
            throw new DaoException("Пайдаланушыны қосу мүмкін болмады: " + user.getUsername(), e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return findOne("SELECT * FROM users WHERE id = ?", String.valueOf(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findOne("SELECT * FROM users WHERE username = ?", username);
    }

    private Optional<User> findOne(String sql, String param) {
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Пайдаланушыны іздеу мүмкін болмады", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM users ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("Пайдаланушылар тізімін оқу мүмкін болмады", e);
        }
        return result;
    }

    @Override
    public boolean updateStatus(int id, AccountStatus status) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE users SET status = ? WHERE id = ?")) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Мәртебені жаңарту мүмкін болмады", e);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Пайдаланушыны өшіру мүмкін болмады", e);
        }
    }

    // >>> [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] БАСТАЛАДЫ — ТРАНЗАКЦИЯ (commit / rollback)
    @Override
    public boolean blockUserWithAudit(int userId, String reason) {
        try (Connection c = db.getConnection()) {
            c.setAutoCommit(false); // транзакцияны бастаймыз
            try {
                // 1-қадам: мәртебені BLOCKED етеміз
                int updated;
                try (PreparedStatement ps = c.prepareStatement("UPDATE users SET status = ? WHERE id = ?")) {
                    ps.setString(1, AccountStatus.BLOCKED.name());
                    ps.setInt(2, userId);
                    updated = ps.executeUpdate();
                }
                if (updated == 0) {
                    c.rollback(); // пайдаланушы табылмады — ештеңе өзгермейді
                    return false;
                }
                // 2-қадам: сол транзакцияда аудит жазбасын қосамыз
                auditLogDao.insert(c, userId, "BLOCK_USER", reason);
                c.commit(); // екі қадам да сәтті — растаймыз
                return true;
            } catch (SQLException e) {
                c.rollback(); // қате болса — барлығын кері қайтарамыз
                throw new DaoException("Транзакция орындалмады, өзгерістер кері қайтарылды", e);
            }
        } catch (SQLException e) {
            throw new DaoException("Қосылым қатесі", e);
        }
    }
    // <<< [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] АЯҚТАЛДЫ — ТРАНЗАКЦИЯ

    private User mapRow(ResultSet rs) throws SQLException {
        return UserFactory.create(
                Role.valueOf(rs.getString("role")),
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("full_name"),
                rs.getString("faculty"),
                rs.getString("password_hash"),
                AccountStatus.valueOf(rs.getString("status")));
    }
}
// <<< [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] АЯҚТАЛДЫ — SQLite + JDBC + DAO

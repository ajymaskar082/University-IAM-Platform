package kz.university.iam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Аудит журналымен жұмыс істейтін DAO. */
public class AuditLogDao {

    private final DatabaseManager db;

    public AuditLogDao(DatabaseManager db) {
        this.db = db;
    }

    /** Берілген (транзакциядағы) қосылым арқылы жазба қосады. */
    public void insert(Connection connection, int userId, String action, String details) throws SQLException {
        String sql = "INSERT INTO audit_log(user_id, action, details, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.setString(4, LocalDateTime.now().toString());
            ps.executeUpdate();
        }
    }

    public List<String> findActionsByUser(int userId) {
        String sql = "SELECT action, details FROM audit_log WHERE user_id = ? ORDER BY id";
        List<String> result = new ArrayList<>();
        try (Connection c = db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("action") + ": " + rs.getString("details"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Аудит журналын оқу мүмкін болмады", e);
        }
        return result;
    }

    public int count() {
        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM audit_log");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DaoException("Аудит журналын санау мүмкін болмады", e);
        }
    }
}

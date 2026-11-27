package kz.university.iam.dao;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.User;

import java.util.List;
import java.util.Optional;

// >>> [ТАЛАП: ДЕРЕКТЕР ҚОРЫ] БАСТАЛАДЫ — DAO үлгісі: UserDao интерфейсі
public interface UserDao {

    int insert(User user);

    Optional<User> findById(int id);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    boolean updateStatus(int id, AccountStatus status);

    boolean delete(int id);

    /** ТРАНЗАКЦИЯ: пайдаланушыны бұғаттау + аудит жазбасы — бірге орындалады немесе мүлде орындалмайды. */
    boolean blockUserWithAudit(int userId, String reason);
}

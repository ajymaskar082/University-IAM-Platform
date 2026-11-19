package kz.university.iam.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// >>> [ТАЛАП: КӨПАҒЫНДЫЛЫҚ] БАСТАЛАДЫ — көп ағынды қауіпсіз Rate Limiter
/**
 * Қате кіру әрекеттерін санайды. Бірнеше ағын бір мезгілде жұмыс істегенде де
 * дұрыс жұмыс істеу үшін ConcurrentHashMap және AtomicInteger қолданылады.
 */
public class LoginAttemptLimiter {

    private final int maxAttempts;
    private final ConcurrentHashMap<String, AtomicInteger> failures = new ConcurrentHashMap<>();

    public LoginAttemptLimiter(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    /** Қате әрекетті тіркейді және ағымдағы санын қайтарады. */
    public int recordFailure(String username) {
        return failures.computeIfAbsent(username, k -> new AtomicInteger()).incrementAndGet();
    }

    public int getFailures(String username) {
        AtomicInteger counter = failures.get(username);
        return counter == null ? 0 : counter.get();
    }

    public boolean isLocked(String username) {
        return getFailures(username) >= maxAttempts;
    }

    public void reset(String username) {
        failures.remove(username);
    }
}

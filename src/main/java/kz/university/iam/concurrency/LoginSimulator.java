package kz.university.iam.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Бірнеше ағыннан бір мезгілде қате кіру әрекеттерін имитациялайды
 * (ExecutorService + Callable).
 */
public class LoginSimulator {

    public static int simulate(LoginAttemptLimiter limiter, String username,
                               int threads, int attemptsPerThread) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            tasks.add(() -> {
                int last = 0;
                for (int j = 0; j < attemptsPerThread; j++) {
                    last = limiter.recordFailure(username);
                }
                return last;
            });
        }
        try {
            pool.invokeAll(tasks); // барлық ағын аяқталғанша күтеді
        } finally {
            pool.shutdown();
        }
        pool.awaitTermination(5, TimeUnit.SECONDS);
        return limiter.getFailures(username);
    }
}
// <<< [ТАЛАП: КӨПАҒЫНДЫЛЫҚ] АЯҚТАЛДЫ

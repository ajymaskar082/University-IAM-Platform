package kz.university.iam.service;

import kz.university.iam.model.AccountStatus;
import kz.university.iam.model.Role;
import kz.university.iam.model.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

// >>> [ТАЛАП: STREAM API] БАСТАЛАДЫ — есептер мен аналитика: filter, map, groupingBy
public class AnalyticsService {

    private final List<User> users;

    public AnalyticsService(List<User> users) {
        this.users = users;
    }

    /** filter: тек белсенді аккаунттар. */
    public List<User> activeUsers() {
        return users.stream()
                .filter(u -> u.getStatus() == AccountStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    /** filter + map: берілген рөлдегі пайдаланушылардың аты-жөндері (әліпби бойынша). */
    public List<String> fullNamesByRole(Role role) {
        return users.stream()
                .filter(u -> u.getRole() == role)
                .map(User::getFullName)
                .sorted()
                .collect(Collectors.toList());
    }

    /** groupingBy: рөл бойынша пайдаланушылар саны. */
    public Map<Role, Long> countByRole() {
        return users.stream()
                .collect(Collectors.groupingBy(
                        User::getRole,
                        () -> new EnumMap<>(Role.class),
                        Collectors.counting()));
    }

    /** groupingBy: факультет бойынша пайдаланушылар саны. */
    public Map<String, Long> countByFaculty() {
        return users.stream()
                .collect(Collectors.groupingBy(
                        User::getFaculty,
                        TreeMap::new,
                        Collectors.counting()));
    }

    /** groupingBy: мәртебе бойынша пайдаланушы логиндері. */
    public Map<AccountStatus, List<String>> usernamesByStatus() {
        return users.stream()
                .collect(Collectors.groupingBy(
                        User::getStatus,
                        () -> new EnumMap<>(AccountStatus.class),
                        Collectors.mapping(User::getUsername, Collectors.toList())));
    }

    /** map + Set: қайталанбайтын факультеттер тізімі. */
    public Set<String> faculties() {
        return users.stream()
                .map(User::getFaculty)
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
// <<< [ТАЛАП: STREAM API] АЯҚТАЛДЫ

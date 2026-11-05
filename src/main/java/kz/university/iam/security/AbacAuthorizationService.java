
package kz.university.iam.security;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AbacAuthorizationService {

    public boolean isAllowedDuringWorkingHours() {
        LocalTime now = LocalTime.now();

        return !now.isBefore(LocalTime.of(8, 0))
                && !now.isAfter(LocalTime.of(20, 0));
    }

    public boolean canAccessResource(
            boolean trustedDevice,
            String userRole
    ) {
        if (userRole == null || userRole.isBlank()) {
            return false;
        }

        boolean validRole = userRole.equals("ADMIN")
                || userRole.equals("TEACHER")
                || userRole.equals("STUDENT");

        return validRole
                && trustedDevice
                && isAllowedDuringWorkingHours();
    }
}

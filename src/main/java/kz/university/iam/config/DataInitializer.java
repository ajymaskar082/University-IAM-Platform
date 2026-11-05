
package kz.university.iam.config;

import kz.university.iam.entity.Role;
import kz.university.iam.entity.Permission;
import kz.university.iam.repository.RoleRepository;
import kz.university.iam.repository.PermissionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {

        return args -> {

            String[] permissions = {
                "USER_READ",
                "USER_CREATE",
                "USER_UPDATE",
                "USER_DELETE",
                "ROLE_MANAGE",
                "AUDIT_READ"
            };

            for (String name : permissions) {
                if (!permissionRepository.existsByName(name)) {
                    permissionRepository.save(new Permission(name));
                }
            }

            createRole(roleRepository, permissionRepository,
                    "ADMIN", permissions);

            createRole(roleRepository, permissionRepository,
                    "TEACHER", "USER_READ");

            createRole(roleRepository, permissionRepository,
                    "STUDENT", "USER_READ");
        };
    }

    private void createRole(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            String roleName,
            String... permissionNames) {

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> new Role(roleName));

        for (String name : permissionNames) {
            Permission permission =
                    permissionRepository.findByName(name)
                            .orElseThrow();

            role.getPermissions().add(permission);
        }

        roleRepository.save(role);
    }
}

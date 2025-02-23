package net.nighthawk.seekersconnect_backend.repo;

import net.nighthawk.seekersconnect_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    User findByUsername(String username);
}

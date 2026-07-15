package dev.github.sterio0o.collectorservice.repository;

import dev.github.sterio0o.collectorservice.model.Source;
import dev.github.sterio0o.collectorservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Set<Source> findSourceById(UUID userId);
}

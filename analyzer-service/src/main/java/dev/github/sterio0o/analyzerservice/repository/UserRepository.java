package dev.github.sterio0o.analyzerservice.repository;

import dev.github.sterio0o.analyzerservice.model.Source;
import dev.github.sterio0o.analyzerservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Set<Source> findSourceById(UUID userId);

}

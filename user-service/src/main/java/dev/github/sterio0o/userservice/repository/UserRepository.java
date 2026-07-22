package dev.github.sterio0o.userservice.repository;

import dev.github.sterio0o.userservice.model.entity.Source;
import dev.github.sterio0o.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByIdAndSources_Id(UUID userId, Long sourceId);

    Page<Source> findSourcesById(UUID userId, Pageable pageable);
}

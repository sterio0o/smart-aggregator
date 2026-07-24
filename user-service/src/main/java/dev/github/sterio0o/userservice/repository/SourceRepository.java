package dev.github.sterio0o.userservice.repository;

import dev.github.sterio0o.userservice.model.entity.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {
    Page<Source> findAll(Pageable pageable);
}

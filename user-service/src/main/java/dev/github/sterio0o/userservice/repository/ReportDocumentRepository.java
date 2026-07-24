package dev.github.sterio0o.userservice.repository;

import dev.github.sterio0o.common.util.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ReportDocumentRepository extends MongoRepository<Report, String> {
    Page<Report> findAllByUserId(String userId, Pageable pageable);
}

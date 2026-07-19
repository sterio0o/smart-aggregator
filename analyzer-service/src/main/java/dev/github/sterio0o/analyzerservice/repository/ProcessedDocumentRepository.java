package dev.github.sterio0o.analyzerservice.repository;

import dev.github.sterio0o.common.util.ProcessedContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedDocumentRepository extends MongoRepository<ProcessedContent, String> {
}

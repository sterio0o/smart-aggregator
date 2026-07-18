package dev.github.sterio0o.collectorservice.repository;

import dev.github.sterio0o.collectorservice.model.RawDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RawDocumentRepository extends MongoRepository<RawDocument, String> {
}

package dev.github.sterio0o.collectorservice.repository;

import dev.github.sterio0o.common.util.AggregateContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RawDocumentRepository extends MongoRepository<AggregateContent, String> {
}

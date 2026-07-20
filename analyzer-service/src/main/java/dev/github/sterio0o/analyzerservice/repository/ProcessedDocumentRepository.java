package dev.github.sterio0o.analyzerservice.repository;

import dev.github.sterio0o.common.util.ProcessedContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessedDocumentRepository extends MongoRepository<ProcessedContent, String> {
    // Быстрый поиск по url для проверки был ли данные контент ранее обработан
    Optional<ProcessedContent> findBySourceUrl(String sourceUrl);

    // Быстрый поиск по списку url для проверки был ли данные контент ранее обработан
    List<ProcessedContent> findAllBySourceUrlIn(List<String> urls);
}

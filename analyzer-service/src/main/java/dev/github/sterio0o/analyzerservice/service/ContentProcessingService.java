package dev.github.sterio0o.analyzerservice.service;

import dev.github.sterio0o.analyzerservice.model.User;
import dev.github.sterio0o.analyzerservice.repository.ProcessedDocumentRepository;
import dev.github.sterio0o.analyzerservice.repository.RawDocumentRepository;
import dev.github.sterio0o.analyzerservice.repository.UserRepository;
import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import dev.github.sterio0o.common.util.ProcessedContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

// Сервис обработки контента. По ключевым словам пользователя находятся статьи и обрабатываются
@Service
@RequiredArgsConstructor
@Slf4j
public class ContentProcessingService {
    private final UserRepository userRepository;
    private final RawDocumentRepository rawDocumentRepository;
    private final ProcessedDocumentRepository processedDocumentRepository;
    private final MongoTemplate mongoTemplate;

    // Преобразовать AggregateContent в ProcessedContent по ключевым словам
    public List<ProcessedContent> processedContent(UUID userId, List<AdapterType> types) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID=" + userId + " не найден"));

        List<String> typesString = types.stream().map(AdapterType::name).toList();
        List<String> keywords = user.getKeywords();

        List<AggregateContent> aggregateContents = rawDocumentRepository.findAllByCategoriesInAndSourceNameIn(keywords, typesString);
        List<String> rawUrls = aggregateContents.stream().map(AggregateContent::getSourceUrl).toList();

        // Ранее обработанный контент
        List<ProcessedContent> alreadyProcessed = processedDocumentRepository.findAllBySourceUrlIn(rawUrls);

        Map<String, ProcessedContent> alreadyProcessedMap = new HashMap<>();
        for (ProcessedContent content : alreadyProcessed) {
            alreadyProcessedMap.put(content.getSourceUrl(), content);
        }

        // Контент который ранее не обрабатывался
        List<AggregateContent> contentsToProcess = aggregateContents.stream()
                .filter(raw -> !alreadyProcessedMap.containsKey(raw.getSourceUrl()))
                .toList();

        List<ProcessedContent> processedContents = new ArrayList<>();

        if (!contentsToProcess.isEmpty()) {
             processedContents = contentsToProcess.stream()
                    .map(this::convertToProcessedContent)
                    .toList();

            try {
                BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ProcessedContent.class);
                bulkOperations.insert(processedContents);
                bulkOperations.execute();
            } catch (BulkOperationException e) {
                log.info("Пропуск дубликатов");
            }
        }

        // Объединяем ранее обработанные статьи и только что обработанные
        List<ProcessedContent> result = new ArrayList<>();
        result.addAll(alreadyProcessed);
        result.addAll(processedContents);

        return result;
    }

    private ProcessedContent convertToProcessedContent(AggregateContent aggregateContent) {
        return ProcessedContent.builder()
                .title(aggregateContent.getTitle())
                .description(aggregateContent.getDescription())
                .content(aggregateContent.getContent())
                .author(aggregateContent.getAuthor())
                .sourceUrl(aggregateContent.getSourceUrl())
                .sourceName(aggregateContent.getSourceName())
                .publishDate(aggregateContent.getPublishDate())
                .categories(aggregateContent.getCategories())
                .processedAt(Instant.now())
                .build();
    }

}

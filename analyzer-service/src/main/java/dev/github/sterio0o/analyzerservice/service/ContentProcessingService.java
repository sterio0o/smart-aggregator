package dev.github.sterio0o.analyzerservice.service;

import dev.github.sterio0o.analyzerservice.model.User;
import dev.github.sterio0o.analyzerservice.repository.ProcessedDocumentRepository;
import dev.github.sterio0o.analyzerservice.repository.RawDocumentRepository;
import dev.github.sterio0o.analyzerservice.repository.UserRepository;
import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import dev.github.sterio0o.common.util.ProcessedContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

// Сервис обработки контента. По ключевым словам пользователя находятся статьи и обрабатываются
@Service
@RequiredArgsConstructor
public class ContentProcessingService {
    private final UserRepository userRepository;
    private final RawDocumentRepository rawDocumentRepository;
    private final ProcessedDocumentRepository processedDocumentRepository;

    // Преобразовать AggregateContent в ProcessedContent по ключевым словам
    public List<ProcessedContent> processedContent(UUID userId, List<AdapterType> types) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь с ID=" + userId + " не найден"));

        List<String> typesString = types.stream().map(AdapterType::name).toList();

        List<String> keywords = user.getKeywords();
        List<AggregateContent> aggregateContents = rawDocumentRepository.findAllByCategoriesInAndSourceNameIn(keywords, typesString);
        List<ProcessedContent> processedContents = aggregateContents.stream()
                .map(this::convertToProcessedContent)
                .toList();

        return processedDocumentRepository.saveAll(processedContents);
    }

    private ProcessedContent convertToProcessedContent(AggregateContent aggregateContent) {
        return ProcessedContent.builder()
                .id(aggregateContent.getId())
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

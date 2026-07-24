package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

// По расписанию будет ходить по выбранным в types сервисам и извлекать оттуда контент
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionSchedulerService {
    private final ContentAggregationService contentAggregationService;
    private final MongoTemplate mongoTemplate;

    private final List<AdapterType> types = List.of(AdapterType.HABR_RSS);

    // Каждый час собирает данные 3_600_000
    @Scheduled(fixedDelay = 60000)
    public void scheduleTask() {
        List<AggregateContent> contents = contentAggregationService.getContentFromAllSource(types);

        if (contents == null || contents.isEmpty()) {
            log.info("scheduleTask не получил никакого контента");
            return;
        }

        try {
            BulkOperations bulkOperation = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, AggregateContent.class);
            bulkOperation.insert(contents);
            bulkOperation.execute();

            log.info("Контент успешно сохранен");
        } catch (BulkOperationException e) {
            log.info("Новые статьи сохранены, дубликаты пропущены");
        }
    }

}

package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.repository.RawDocumentRepository;
import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

// По расписанию будет ходить по выбранным в types сервисам и извлекать оттуда контент
@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionSchedulerService {
    private final ContentAggregationService contentAggregationService;
    private final RawDocumentRepository rawDocumentRepository;

    private final List<AdapterType> types = List.of(AdapterType.HABR_RSS);

    // Каждый час собирает данные
    @Scheduled(fixedDelay = 3_600_000)
    public void scheduleTask() {
        List<AggregateContent> contents = contentAggregationService.getContentFromAllSource(types);

        if (contents == null) {
            log.info("scheduleTask не получил никакого контента");
            return;
        }

        rawDocumentRepository.saveAll(contents);
        log.info("Контент успешно сохранен");
    }

}

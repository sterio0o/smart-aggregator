package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.kafka.KafkaProducer;
import dev.github.sterio0o.collectorservice.model.Source;
import dev.github.sterio0o.collectorservice.model.User;
import dev.github.sterio0o.collectorservice.repository.UserRepository;
import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

// ПЕРЕНЕСТИ В Analyzer Service и по индивидуальному расписанию брать данные и работать с ними
@Service
@RequiredArgsConstructor
@Slf4j
public class StartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ContentAggregationService contentAggregationService;
    private final CollectionSchedulerService collectionSchedulerService;
    private final KafkaProducer kafkaProducer;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            Runnable task = () -> {
                try {
                    // Извлечь все подписки юзера
                    Set<Source> sources = userRepository.findSourceById(user.getId());

                    if (sources == null) {
                        log.info("Список подписок пуст, user: {}", user.getId());
                        return;
                    }

                    List<AdapterType> types = sources.stream().map(Source::getName).toList();
                    List<AggregateContent> contents = contentAggregationService.getContentFromAllSource(types);

                    log.info("Runnable успешно выполнен и событие отправлено в Kafka, user: {}", user.getId());
                } catch (Exception e) {
                    log.info("Ошибка в Runnable user: {}", user.getId());
                }
            };

            collectionSchedulerService.scheduleTask(user.getId(), user.getReportFrequency(), task);
        }
    }
}

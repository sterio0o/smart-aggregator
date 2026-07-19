package dev.github.sterio0o.analyzerservice.service;

import dev.github.sterio0o.analyzerservice.model.Source;
import dev.github.sterio0o.analyzerservice.model.User;
import dev.github.sterio0o.analyzerservice.repository.UserRepository;
import dev.github.sterio0o.common.util.AdapterType;
import dev.github.sterio0o.common.util.AggregateContent;
import dev.github.sterio0o.common.util.ProcessedContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ContentProcessingService contentProcessingService;
    private final CollectionSchedulerService collectionSchedulerService;
    private final AiService aiService;

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
                    // Обрабатывает контент
                    List<ProcessedContent> contents = contentProcessingService.processedContent(user.getId(), types);

                    aiService.generateReport(user.getId(), contents, user.getKeywords());

                    // Дописать отправку события через Kafka в Delivery Service

                    log.info("Runnable успешно выполнен и событие отправлено в Kafka, user: {}", user.getId());
                } catch (Exception e) {
                    log.info("Ошибка в Runnable user: {}", user.getId());
                }
            };

            collectionSchedulerService.scheduleTask(user.getId(), user.getReportFrequency(), task);
        }
    }
}

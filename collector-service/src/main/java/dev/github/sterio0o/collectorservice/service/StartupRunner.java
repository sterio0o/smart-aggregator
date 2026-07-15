package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.model.Source;
import dev.github.sterio0o.collectorservice.model.User;
import dev.github.sterio0o.collectorservice.repository.UserRepository;
import dev.github.sterio0o.common.util.AdapterType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ContentAggregationService contentAggregationService;
    private final CollectionSchedulerService collectionSchedulerService;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            // Извлечь все подписки юзера
            Set<Source> sources = userRepository.findSourceById(user.getId());

            Runnable task = () -> {
                List<AdapterType> types = sources.stream().map(Source::getName).toList();
                contentAggregationService.getContentFromAllSource(types);
            };

            collectionSchedulerService.scheduleTask(user.getId(), user.getReportFrequency().toString(), task);
        }
    }
}

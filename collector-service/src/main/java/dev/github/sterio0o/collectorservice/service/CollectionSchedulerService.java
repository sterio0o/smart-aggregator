package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

// ПЕРЕНЕСТИ В Analyzer Service
// Сервис, который будет по таймеру обходить провайдеров и собирать контент
@Service
@RequiredArgsConstructor
public class CollectionSchedulerService {
    private final ThreadPoolTaskScheduler taskScheduler;

    // Хранилище задач
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // Планирование задачи пользователя
    public void scheduleTask(UUID userId, Duration reportFrequency, Runnable task) {
        cancelUserTask(userId);
        if (reportFrequency != null) {
            ScheduledFuture<?> future = taskScheduler.schedule(task, new PeriodicTrigger(reportFrequency));
            scheduledTasks.put(userId, future);
        }
    }

    // Завершить задачу
    public void cancelUserTask(UUID userId) {
        ScheduledFuture<?> task = scheduledTasks.remove(userId);
        if (task != null) task.cancel(false);
    }
}

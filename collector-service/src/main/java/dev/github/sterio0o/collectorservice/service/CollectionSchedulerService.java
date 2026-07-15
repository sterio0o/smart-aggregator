package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

// Сервис, который будет по таймеру обходить провайдеров и собирать контент
@Service
@RequiredArgsConstructor
public class CollectionSchedulerService {
    private final ThreadPoolTaskScheduler taskScheduler;

    // Хранилище задач
    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // Планирование задачи пользователя
    public void scheduleTask(UUID userId, String cronExpression, Runnable task) {
        cancelUserTask(userId);
        if (!cronExpression.isEmpty()) {
            ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cronExpression));
            scheduledTasks.put(userId, future);
        }
    }

    // Завершить задачу
    public void cancelUserTask(UUID userId) {
        ScheduledFuture<?> task = scheduledTasks.remove(userId);
        if (task != null) task.cancel(false);
    }
}

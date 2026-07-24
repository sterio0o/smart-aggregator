package dev.github.sterio0o.deliveryservice.kafka;

import dev.github.sterio0o.common.event.SuccessfulDataAnalysisEvent;
import dev.github.sterio0o.deliveryservice.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final MailSenderService mailSenderService;

    @KafkaListener(topics = "success-data-analysis-event", groupId = "notification")
    public void successDataAnalysisListener(SuccessfulDataAnalysisEvent event) {
        log.info("Kafka consumer accepted event: " + event);
        mailSenderService.handleSuccessfulDataAnalysisEvent(event);
    }
}

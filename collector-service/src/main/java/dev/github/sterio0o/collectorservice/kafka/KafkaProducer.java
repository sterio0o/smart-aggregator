package dev.github.sterio0o.collectorservice.kafka;

import dev.github.sterio0o.common.event.TransmissionCollectedDataEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private static final String KAFKA_EVENT = "transmission-collected-data-event";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(TransmissionCollectedDataEvent event) {
        kafkaTemplate.send(KAFKA_EVENT, event);
    }
}

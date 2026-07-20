package dev.github.sterio0o.analyzerservice.kafka;

import dev.github.sterio0o.common.event.SuccessfulDataAnalysisEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private static final String EVENT = "successful-data-analysis-event";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(SuccessfulDataAnalysisEvent event) {
        kafkaTemplate.send(EVENT, event);
    }

}

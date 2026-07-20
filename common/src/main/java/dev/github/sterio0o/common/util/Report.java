package dev.github.sterio0o.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "report")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Report {
    @Id
    private String id;
    private UUID userId;

    private String aiSummary; // Отчет нейросети
    private Instant createdAt;

    private List<String> sourceContentIds; // Ссылка на документы из processed, по которым был сделан отчет
    private List<String> keywords; // Ключевые слова интересующие пользователя и по которым делался отчет
}

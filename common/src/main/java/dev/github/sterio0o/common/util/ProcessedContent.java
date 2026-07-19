package dev.github.sterio0o.common.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// Обработанные данные
@Document(collection = "processed")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProcessedContent {
    @Id
    private String id;
    private String title;
    private String description;
    private String content;
    private String author;
    private String sourceUrl;
    private String sourceName;
    private String publishDate;
    private List<String> categories;

    private Instant processedAt;
}

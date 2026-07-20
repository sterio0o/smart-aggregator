package dev.github.sterio0o.common.util;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

// "Сырые" данные из Collector Service, сохраняются в коллекцию raw
@Document(collection = "raw")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AggregateContent {
    @Id
    private String id;
    private String title;
    private String description;
    private String content;
    private String author;

    @Indexed(unique = true)
    private String sourceUrl; // защита от дубликатов - уникальная url ссылка

    private String sourceName;
    private String publishDate;
    private List<String> categories;
}

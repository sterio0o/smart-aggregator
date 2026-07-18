package dev.github.sterio0o.common.util;

import java.util.List;

// "Сырые" данные из Collector Service, сохраняются в коллекцию raw
public record AggregateContent(
        String id,
        String title,
        String description,
        String content,
        String author,
        String sourceUrl,
        String sourceName,
        String publishDate,
        List<String> categories
) {
}

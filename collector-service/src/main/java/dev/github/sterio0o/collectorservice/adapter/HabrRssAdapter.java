package dev.github.sterio0o.collectorservice.adapter;

import dev.github.sterio0o.collectorservice.dto.habr.HabrItem;
import dev.github.sterio0o.collectorservice.dto.habr.HabrRss;
import dev.github.sterio0o.collectorservice.exception.HabrRssException;
import dev.github.sterio0o.collectorservice.interfaces.AggregateProvider;
import dev.github.sterio0o.collectorservice.interfaces.HabrRssServiceClient;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Адаптер для получения Habr RSS ленты
@Component
@RequiredArgsConstructor
@Slf4j
public class HabrRssAdapter implements AggregateProvider {

    private final HabrRssServiceClient habrRssServiceClient;

    @Override
    public List<AggregateContent> fetchContent() {
        // Список RSS лент для обхода
        List<String> hubNames = List.of("devops", "programming", "infosecurity", "artificial_intelligence", "java");
        List<AggregateContent> result = new ArrayList<>();
        List<AggregateContent> currentList;

        for (String hub : hubNames) {
            try {
                HabrRss rss = habrRssServiceClient.getHabrRssFeeds(hub);

                if (rss == null || rss.channel() == null || rss.channel().items() == null) {
                    throw new HabrRssException("Habr RSS пуст!");
                }

                currentList = rss.channel().items().stream()
                        .map(this::convertToAggregateContent)
                        .toList();

                result.addAll(currentList);
            } catch (Exception e) {
                log.info("Не удалось распарсить Habr RSS ({}): {}", hub, e.getMessage());
            }
        }

        return result;
    }

    private AggregateContent convertToAggregateContent(HabrItem item) {
        String clearContent = item.description() != null ? Jsoup.parse(item.description()).text() : "";

        return AggregateContent.builder()
                .title(item.title())
                .description(clearContent)
                .content(clearContent)
                .author("Unknown")
                .sourceUrl(item.link())
                .sourceName("HABR")
                .publishDate(item.pubDate())
                .createdAt(Instant.now())
                .categories(item.categories())
                .build();
    }
}

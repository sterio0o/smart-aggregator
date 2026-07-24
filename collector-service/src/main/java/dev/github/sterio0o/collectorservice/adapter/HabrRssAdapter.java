package dev.github.sterio0o.collectorservice.adapter;

import dev.github.sterio0o.collectorservice.dto.habr.HabrItem;
import dev.github.sterio0o.collectorservice.dto.habr.HabrRss;
import dev.github.sterio0o.collectorservice.exception.HabrRssException;
import dev.github.sterio0o.collectorservice.interfaces.AggregateProvider;
import dev.github.sterio0o.collectorservice.interfaces.HabrRssServiceClient;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
        try {
            HabrRss rss = habrRssServiceClient.getHabrRssFeeds();

            if (rss == null || rss.channel() == null || rss.channel().items() == null) {
                throw new HabrRssException("Habr RSS пуст!");
            }

            return rss.channel().items().stream()
                    .map(this::convertToAggregateContent)
                    .toList();
        } catch (Exception e) {
            log.info("Не удалось распарсить Habr RSS! " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private AggregateContent convertToAggregateContent(HabrItem item) {
        return AggregateContent.builder()
                .title(item.title())
                .description(item.description())
                .content(item.description())
                .author("Unknown")
                .sourceUrl(item.link())
                .sourceName("HABR")
                .publishDate(item.pubData())
                .createdAt(Instant.now())
                .categories(item.categories())
                .build();
    }
}

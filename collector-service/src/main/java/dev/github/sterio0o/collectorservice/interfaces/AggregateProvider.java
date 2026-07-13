package dev.github.sterio0o.collectorservice.interfaces;


import dev.github.sterio0o.common.util.AggregateContent;

import java.util.List;

// Интерфейс для единообразия реализации адаптеров
public interface AggregateProvider {
    List<AggregateContent> fetchContent();
}

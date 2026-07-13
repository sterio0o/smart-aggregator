package dev.github.sterio0o.collectorservice.service;

import dev.github.sterio0o.collectorservice.factory.AdapterType;
import dev.github.sterio0o.collectorservice.factory.AggregateExtractFactory;
import dev.github.sterio0o.collectorservice.interfaces.AggregateProvider;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Сервис для использования адаптеров
@Service
@RequiredArgsConstructor
public class ContentAggregationService {
    private final AggregateExtractFactory factory;

    // Метод для получения контента из определенного источника
    public List<AggregateContent> getContentFromSource(AdapterType type) {
        AggregateProvider provider = factory.createAdapter(type);

        return provider.fetchContent();
    }

    // Метод для получения контента из всех выбранных источников
    public List<AggregateContent> getContentFromAllSource(List<AdapterType> types) {
        List<AggregateContent> allContent = new ArrayList<>();
        for (AdapterType type : types) {
            allContent.addAll(getContentFromSource(type));
        }

        return allContent;
    }
}

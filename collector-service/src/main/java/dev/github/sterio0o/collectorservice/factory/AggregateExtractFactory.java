package dev.github.sterio0o.collectorservice.factory;

import dev.github.sterio0o.collectorservice.interfaces.AggregateProvider;
import dev.github.sterio0o.collectorservice.service.HabrRssAdapter;
import dev.github.sterio0o.common.util.AdapterType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AggregateExtractFactory {

    private final HabrRssAdapter habrRssAdapter;

    public AggregateProvider createAdapter(AdapterType type) {
        switch (type) {
            case HABR_RSS -> {
                return habrRssAdapter;
            }
            default -> throw new IllegalArgumentException("Неизвестный тип адаптера: " + type);
        }
    }

}

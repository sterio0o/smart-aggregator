package dev.github.sterio0o.collectorservice.controller;

import dev.github.sterio0o.collectorservice.factory.AdapterType;
import dev.github.sterio0o.collectorservice.service.ContentAggregationService;
import dev.github.sterio0o.common.util.AggregateContent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
public class CollectorController {
    private final ContentAggregationService contentAggregationService;

    // Получить контент из конкретного источника
    @GetMapping
    public ResponseEntity<List<AggregateContent>> getContentFromSource(
            @RequestParam AdapterType type
    ) {
        return ResponseEntity.ok(contentAggregationService.getContentFromSource(type));
    }

    // Получить контент из всех выбранных источников
    @GetMapping("/all")
    public ResponseEntity<List<AggregateContent>> getContentFromAllSource(
            @RequestParam List<AdapterType> types
    ) {
        return ResponseEntity.ok(contentAggregationService.getContentFromAllSource(types));
    }
}

package dev.github.sterio0o.common.event;

import dev.github.sterio0o.common.util.AggregateContent;

import java.util.List;
import java.util.UUID;

public record TransmissionCollectedDataEvent(
        UUID userId,
        List<AggregateContent> contents
) {
}

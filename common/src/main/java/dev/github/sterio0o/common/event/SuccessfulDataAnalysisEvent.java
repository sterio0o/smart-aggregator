package dev.github.sterio0o.common.event;

import dev.github.sterio0o.common.util.Report;

import java.util.List;
import java.util.UUID;

public record SuccessfulDataAnalysisEvent(
        UUID userId,
        String email,
        String deliveryMethod,
        List<String> keywords,
        Report report
) {
}

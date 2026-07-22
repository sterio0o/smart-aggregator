package dev.github.sterio0o.userservice.model.dto;

import dev.github.sterio0o.common.util.Report;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Getter
@Setter
public class ReportResponseDto {
    private String id;
    private String aiSummary; // Отчет нейросети
    private Instant createdAt;
    private List<String> sourceContentIds; // Ссылка на документы из processed, по которым был сделан отчет
    private List<String> keywords; // Ключевые слова интересующие пользователя и по которым делался отчет

    public static ReportResponseDto convertToDto(Report entity) {
        return new ReportResponseDto(
                entity.getId(),
                entity.getAiSummary(),
                entity.getCreatedAt(),
                entity.getSourceContentIds(),
                entity.getKeywords()
        );
    }
}

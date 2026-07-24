package dev.github.sterio0o.userservice.model.dto;

import dev.github.sterio0o.userservice.model.entity.Source;
import lombok.*;


@Data
@Builder
@Getter
@Setter
public class SourceResponseDto {
    private Long id;
    private String name;
    private String sourceUrl;
    private boolean isSubscribe;

    public static SourceResponseDto convertToDto(Source entity, boolean isSubscribe) {
        return new SourceResponseDto(
                entity.getId(),
                entity.getName().name(),
                entity.getSourceUrl(),
                isSubscribe
        );
    }
}

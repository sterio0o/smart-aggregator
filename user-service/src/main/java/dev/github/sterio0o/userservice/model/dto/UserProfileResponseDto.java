package dev.github.sterio0o.userservice.model.dto;

import dev.github.sterio0o.userservice.model.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Data
@Builder
@Getter
@Setter
public class UserProfileResponseDto {
    private String email;
    private List<String> keywords;
    private String deliveryMethod;
    private Duration reportFrequency;

    public static  UserProfileResponseDto convertToDto(User entity) {
        return new UserProfileResponseDto(
                entity.getEmail(),
                entity.getKeywords(),
                entity.getDeliveryMethod(),
                entity.getReportFrequency()
        );
    }
}

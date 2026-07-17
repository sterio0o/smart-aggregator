package dev.github.sterio0o.userservice.model.dto;

import java.util.List;

public record AuthResponseDto(
        String token,
        String email,
        List<String> roles
) {
}
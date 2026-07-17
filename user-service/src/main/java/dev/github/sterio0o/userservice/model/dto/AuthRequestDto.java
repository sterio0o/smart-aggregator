package dev.github.sterio0o.userservice.model.dto;

public record AuthRequestDto(
        String email,
        String password
) {
}
package dev.github.sterio0o.userservice.controller;

import dev.github.sterio0o.common.security.JwtService;
import dev.github.sterio0o.userservice.model.dto.AuthRequestDto;
import dev.github.sterio0o.userservice.model.dto.AuthResponseDto;
import dev.github.sterio0o.userservice.model.entity.User;
import dev.github.sterio0o.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthRequestDto requestDto) {
        User user = authService.register(requestDto);
        String token = jwtService.generateToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getRoles()
        );

        return ResponseEntity.ok(new AuthResponseDto(token, user.getEmail(), user.getRoles()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        User user = authService.authenticate(requestDto.email(), requestDto.password());
        String token = jwtService.generateToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getRoles()
        );

        return ResponseEntity.ok(new AuthResponseDto(token, user.getEmail(), user.getRoles()));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        boolean isValid = jwtService.isTokenValid(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}

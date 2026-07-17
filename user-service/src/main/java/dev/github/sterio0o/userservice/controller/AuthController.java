package dev.github.sterio0o.userservice.controller;

import dev.github.sterio0o.userservice.model.dto.AuthRequestDto;
import dev.github.sterio0o.userservice.model.dto.AuthResponseDto;
import dev.github.sterio0o.userservice.model.entity.User;
import dev.github.sterio0o.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        User user = userService.register(requestDto);
        String token = tokenService.

        return ResponseEntity.ok(new AuthResponseDto(token, user.getEmail(), user.getRoles()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        User user = userService.authenticate(requestDto.email(), requestDto.password());


        return ResponseEntity.ok(new AuthResponseDto(token, user.getEmail(), user.getRoles()));
    }
}
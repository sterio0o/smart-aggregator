package dev.github.sterio0o.userservice.service;

import dev.github.sterio0o.userservice.model.dto.AuthRequestDto;
import dev.github.sterio0o.userservice.model.entity.User;
import dev.github.sterio0o.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(AuthRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .roles(List.of("USER"))
                .build();

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid authentification"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid authentification");
        }

        return user;
    }
}
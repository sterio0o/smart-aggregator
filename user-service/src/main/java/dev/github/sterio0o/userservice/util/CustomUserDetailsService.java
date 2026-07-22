package dev.github.sterio0o.userservice.util;

import dev.github.sterio0o.userservice.model.entity.User;
import dev.github.sterio0o.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User с email=" + username + " не найден"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getId())) // Чтобы AuthenticationPrincipal возвращал userId
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).toList())
                .build();
    }
}

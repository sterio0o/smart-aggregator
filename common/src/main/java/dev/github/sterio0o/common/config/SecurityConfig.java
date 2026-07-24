package dev.github.sterio0o.common.config;

import dev.github.sterio0o.common.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Конфигурация для REST API
    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")          // Применяется к запросам начинающимся на api
                .csrf(AbstractHttpConfigurer::disable) // Отключает CSRF защиту (для REST API не нужна)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Отключает создание HTTP сессии
                // Пути которые не требуют авторизации (не нужен JWT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**"      // Логин и регистрация
                        ).permitAll()
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/web/auth/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/web/auth/login")
                        .loginProcessingUrl("/web/auth/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/web/profiles/sources", true) // Куда будет перенаправлять после логина
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/web/auth/logout")
                        .logoutSuccessUrl("/web/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")    // Очищает куки сессии в браузере
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

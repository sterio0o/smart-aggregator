package dev.github.sterio0o.userservice.controller;

import dev.github.sterio0o.common.security.JwtService;
import dev.github.sterio0o.userservice.model.dto.AuthRequestDto;
import dev.github.sterio0o.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/auth")
@RequiredArgsConstructor
public class WebAuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            Model model
    ) {
        try {
            AuthRequestDto authRequest = new AuthRequestDto(email, password);
            authService.register(authRequest);
            return "redirect:/web/auth/login?success=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }
}

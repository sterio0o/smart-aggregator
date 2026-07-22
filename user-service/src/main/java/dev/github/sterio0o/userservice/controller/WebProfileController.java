package dev.github.sterio0o.userservice.controller;

import dev.github.sterio0o.userservice.model.dto.ReportResponseDto;
import dev.github.sterio0o.userservice.model.dto.SourceResponseDto;
import dev.github.sterio0o.userservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/web/profiles")
@RequiredArgsConstructor
public class WebProfileController {
    private final ProfileService profileService;

    // Получить все источники
    @GetMapping("/sources")
    public String getSourcesPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Model model
    ) {
        String userId = userDetails.getUsername();      // В username записан id

        Page<SourceResponseDto> sourcesPage = profileService.getAllSourceForUser(UUID.fromString(userId), pageable);
        model.addAttribute("sourcesPage", sourcesPage);
        return "sources";
    }

    // Получить все источники на которые подписан пользователь
    @GetMapping("/my-sources")
    public String getMySourcesPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Model model
    ) {
        String userId = userDetails.getUsername();      // В username записан id

        Page<SourceResponseDto> sourcesPage = profileService.getAllSubscribe(UUID.fromString(userId), pageable);
        model.addAttribute("sourcesPage", sourcesPage);
        return "my-sources";
    }

    // Получить все отчеты пользователя
    @GetMapping("/my-reports")
    public String getMyReportPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Model model
    ) {
        String userId = userDetails.getUsername();

        Page<ReportResponseDto> reportsPage = profileService.getMyReports(userId, pageable);
        model.addAttribute("reportsPage", reportsPage);
        return "my-reports";
    }
}

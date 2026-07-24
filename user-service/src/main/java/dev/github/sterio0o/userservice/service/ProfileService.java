package dev.github.sterio0o.userservice.service;

import dev.github.sterio0o.common.util.Report;
import dev.github.sterio0o.userservice.exception.ReportNotFoundException;
import dev.github.sterio0o.userservice.exception.SourceNotFoundException;
import dev.github.sterio0o.userservice.exception.UserNotFoundException;
import dev.github.sterio0o.userservice.model.dto.ReportResponseDto;
import dev.github.sterio0o.userservice.model.dto.SourceResponseDto;
import dev.github.sterio0o.userservice.model.dto.UpdateUserProfileRequestDto;
import dev.github.sterio0o.userservice.model.dto.UserProfileResponseDto;
import dev.github.sterio0o.userservice.model.entity.Source;
import dev.github.sterio0o.userservice.model.entity.User;
import dev.github.sterio0o.userservice.repository.ReportDocumentRepository;
import dev.github.sterio0o.userservice.repository.SourceRepository;
import dev.github.sterio0o.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;
    private final ReportDocumentRepository reportDocumentRepository;

    // Получить источник по id
    public SourceResponseDto getById(UUID userId, Long id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source с ID=" + id + " не найден"));

        boolean isSubscribe = userRepository.existsByIdAndSources_Id(userId, id);

        return SourceResponseDto.convertToDto(source, isSubscribe);
    }

    // Получить все источники с информацией на какие подписан, а на какие нет
    public Page<SourceResponseDto> getAllSourceForUser(UUID userId, Pageable pageable) {
        Page<Source> sources = sourceRepository.findAll(pageable);
        return sources.map(source -> {
            boolean isSubscribe = userRepository.existsByIdAndSources_Id(userId, source.getId());
            return SourceResponseDto.convertToDto(source, isSubscribe);
        });
    }

    // Получить все источники на которые подписан пользователь
    public Page<SourceResponseDto> getAllSubscribe(UUID userId, Pageable pageable) {
        Page<Source> sourcePage = userRepository.findSourcesById(userId, pageable);
        return sourcePage.map(source -> SourceResponseDto.convertToDto(source, true));
    }

    // Получить все отчеты (report) с пагинацией
    public Page<ReportResponseDto> getMyReports(String userId, Pageable pageable) {
        Page<Report> reportPage = reportDocumentRepository.findAllByUserId(userId, pageable);
        return reportPage.map(ReportResponseDto::convertToDto);
    }

    // Получить пользователя по id
    public UserProfileResponseDto getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с ID=" + userId + " не найден"));

        return UserProfileResponseDto.convertToDto(user);
    }

    // Обновить профиль пользователя
    @Transactional
    public UserProfileResponseDto updateUserProfile(UUID userId, UpdateUserProfileRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User с ID=" + userId + " не найден"));

        if (!requestDto.getKeywords().isEmpty())
            user.setKeywords(requestDto.getKeywords());

        if (!requestDto.getDeliveryMethod().isEmpty())
            user.setDeliveryMethod(requestDto.getDeliveryMethod());

        if (requestDto.getReportFrequency() != null)
            user.setReportFrequency(requestDto.getReportFrequency());

        User savedUser = userRepository.save(user);
        return UserProfileResponseDto.convertToDto(user);
    }
}

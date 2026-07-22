package dev.github.sterio0o.userservice.service;

import dev.github.sterio0o.userservice.exception.SourceNotFoundException;
import dev.github.sterio0o.userservice.model.dto.SourceResponseDto;
import dev.github.sterio0o.userservice.model.entity.Source;
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

    public SourceResponseDto getById(UUID userId, Long id) {
        Source source = sourceRepository.findById(id)
                .orElseThrow(() -> new SourceNotFoundException("Source с ID=" + id + " не найден"));

        boolean isSubscribe = userRepository.existsByIdAndSources_Id(userId, id);

        return SourceResponseDto.convertToDto(source, isSubscribe);
    }

    // Получить все источники с информацией на какие подписан, а на какие нет
    public Page<SourceResponseDto> getAllSourceForUser(UUID userId, Pageable pageable) {
        Page<Source> sources = sourceRepository.findAll(pageable);

        if (sources.isEmpty())
            throw new SourceNotFoundException("Ни один источник не найден");

        return sources.map(source -> {
            boolean isSubscribe = userRepository.existsByIdAndSources_Id(userId, source.getId());
            return SourceResponseDto.convertToDto(source, isSubscribe);
        });
    }

    // Получить все источники на которые подписан пользователь
    public Page<SourceResponseDto> getAllSubscribe(UUID userId, Pageable pageable) {
        Page<Source> sourcePage = userRepository.findSourcesById(userId, pageable);

        if (sourcePage.isEmpty())
            throw new SourceNotFoundException("Ни один источник не найден");

        return sourcePage.map(source -> SourceResponseDto.convertToDto(source, true));
    }
}

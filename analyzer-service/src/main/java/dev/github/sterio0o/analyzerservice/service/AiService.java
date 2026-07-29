package dev.github.sterio0o.analyzerservice.service;

import dev.github.sterio0o.analyzerservice.repository.ReportDocumentRepository;
import dev.github.sterio0o.common.util.ProcessedContent;
import dev.github.sterio0o.common.util.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import javax.swing.text.StringContent;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final ChatClient chatClient;
    private final ReportDocumentRepository reportDocumentRepository;

    public Report generateReport(UUID userid, List<ProcessedContent> contents, List<String> keywords) {
        try {
            log.info("Генерация отчета началась");
            String stringContents = contents.stream()
                    .map(c -> String.format("Заголовок: %s\nОписание: %s\nТекст: %s", c.getTitle(), c.getDescription(), c.getContent()))
                    .collect(Collectors.joining("\n\n"));

            String template = """
                Ты профессиональный аналитик данных. На основе предоставленных данных и ключевых тем, 
                которые интересуют пользователя, сформируй краткий отчет (aiSummary).
                 
                Интересующие пользователя темы: {keywords}
                
                Материалы для анализа: {contents}
                
                Ты должен вернуть данные строго в формате JSON без лишних пояснений и на русском языке
                """;

            Report report = chatClient.prompt().user(userSpec -> userSpec
                    .text(template)
                    .param("keywords", String.join(", ", keywords))
                    .param("contents", stringContents)
            )
                    .call()
                    .entity(Report.class);

            if (report == null) {
                throw new RuntimeException("Не удалось сгенерировать отчет!");
            }

            report.setUserId(String.valueOf(userid));
            report.setCreatedAt(Instant.now());
            report.setSourceContentIds(contents.stream().map(ProcessedContent::getSourceUrl).toList());
            report.setKeywords(keywords);

            reportDocumentRepository.save(report);

            log.info("Генерация отчета прошла успешно, отчет сохранен");
            return report;
        } catch (Exception e) {
            if (e.getMessage().contains("402") || e.getMessage().contains("Insufficient Balance")) {
                log.error("Недостаточно средств для работы AI");
            }

            throw e;
        }
    }
}

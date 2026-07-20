package dev.github.sterio0o.analyzerservice.service;

import dev.github.sterio0o.analyzerservice.repository.ReportDocumentRepository;
import dev.github.sterio0o.common.util.ProcessedContent;
import dev.github.sterio0o.common.util.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiService {
    private final ChatModel chatModel;
    private final BeanOutputConverter<Report> reportConverter;
    private final ReportDocumentRepository reportDocumentRepository;

    public Report generateReport(UUID userid, List<ProcessedContent> contents, List<String> keywords) {
        String stringContents = contents.stream()
                .map(c -> String.format("Заголовок: %s\nОписание: %s\nТекст: %s", c.getTitle(), c.getDescription(), c.getContent()))
                .toString();

        String template = """
                Ты профессиональный аналитик данных. На основе предоставленных данных и ключевых тем, 
                которые интересуют пользователя, сформируй краткий отчет (aiSummary).
                 
                Интересующие пользователя темы: {keywords}
                
                Материалы для анализа: {contents}
                
                Ты должен вернуть данные строго в формате JSON, соответствующем следующей схеме: {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);

        Prompt prompt = promptTemplate.create(Map.of(
                "keywords", String.join(", ", keywords),
                "contents", stringContents,
                "format", reportConverter.getFormat()
        ));

        String response = chatModel.call(prompt).getResult().getOutput().getText();
        Report report = reportConverter.convert(response);

        if (report == null) {
            throw new RuntimeException("Не удалось сгенерировать отчет!");
        }

        report.setUserId(userid);
        report.setCreatedAt(Instant.now());
        report.setSourceContentIds(contents.stream().map(ProcessedContent::getId).toList());
        report.setKeywords(keywords);

        reportDocumentRepository.save(report);

        return report;
    }
}

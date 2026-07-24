package dev.github.sterio0o.deliveryservice.service;

import dev.github.sterio0o.common.event.SuccessfulDataAnalysisEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void handleSuccessfulDataAnalysisEvent(SuccessfulDataAnalysisEvent event) {
        String to = event.email();

        if (to == null || to.isBlank()) {
            log.error("Email is null or empty");
            return;
        }

        String subject = "Отчет по мониторингу источников";
        String body = String.format("""
                Здравствуйте!
                
                Наша система проанализировала и подготовила отчет по вашим источникам.
                
                Ключевые слова: %s
                Дата отчета: %s
                
                Отчет: %s
                
                Благодарим вас за использование сервиса!
                """,
                event.keywords(),
                LocalDateTime.now(),
                event.report()
        );

        sendEmail(to, subject, body);
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(body);

            mailSender.send(mailMessage);
            log.info("Отправка отчета по Email");
        } catch (Exception e) {
            log.error("Ошибка отправка отчета по Email");
        }
    }
}

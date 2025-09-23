package br.com.creditas.loansimulator.infrastructure.gateway.email.impl;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.infrastructure.gateway.email.MailServiceAdapter;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceAdapterImpl implements MailServiceAdapter {
    private final JavaMailSender javaMailSender;


    @Override
    public void sendEmail(LoanSimulation loanSimulation, String content) {
        var recipientEmail = loanSimulation.getPerson().getEmail();

        try {
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@loansimulator.com");
            helper.setTo(recipientEmail);
            helper.setSubject("Sua Simulação de Empréstimo foi Calculada!");

            helper.setText(content, true);

            javaMailSender.send(message);
            log.info("Email successfully sent to {}", recipientEmail);

        } catch (MessagingException e) {
            log.error("Error while creating or setting email message for {}: {}", recipientEmail, e.getMessage(), e);
        } catch (MailException e) {
            log.error("Error while sending email to {}: {}", recipientEmail, e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while sending email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }
}

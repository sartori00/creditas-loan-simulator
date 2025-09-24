package br.com.creditas.loansimulator.infrastructure.entrypoint.consumer;

import br.com.creditas.loansimulator.infrastructure.gateway.email.MailServiceAdapter;
import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailNotificationConsumer {

    private final ObjectMapper objectMapper;
    private final MailServiceAdapter mailServiceAdapter;

    @SqsListener("${sqs.queue.loan-simulator.sendmail.listener}")
    public void receiveMessage(String message) throws JsonProcessingException {
        var emailNotificationDTO = objectMapper.readValue(message, EmailNotificationDTO.class);

        mailServiceAdapter.sendEmail(emailNotificationDTO.content(), emailNotificationDTO.recipientEmail());
    }
}

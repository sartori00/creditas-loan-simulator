package br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl;

import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.EmailNotificationProducer;
import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationProducerImpl implements EmailNotificationProducer {

  @Value("${sqs.queue.loan-simulator.sendmail.producer}")
  private String sendMailQueue;

  private final SqsTemplate sqsTemplate;
  private final ObjectMapper objectMapper;

  public EmailNotificationProducerImpl(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
    this.sqsTemplate = sqsTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public void sendToEmailNotification(EmailNotificationDTO dto) {
    try {
      sqsTemplate.send(sendMailQueue, objectMapper.writeValueAsString(dto));

      log.info("Sent to email notification for {}", dto.recipientEmail());
    } catch (JsonProcessingException e) {
      log.error("Error on sent email notification for {}", dto.recipientEmail());
    }
  }
}

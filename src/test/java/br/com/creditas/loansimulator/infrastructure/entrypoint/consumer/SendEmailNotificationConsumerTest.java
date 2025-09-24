package br.com.creditas.loansimulator.infrastructure.entrypoint.consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.infrastructure.gateway.email.MailServiceAdapter;
import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendEmailNotificationConsumerTest {

  @Mock private ObjectMapper objectMapper;

  @Mock private MailServiceAdapter mailServiceAdapter;

  @InjectMocks private SendEmailNotificationConsumer sendEmailNotificationConsumer;

  private String validJsonMessage;
  private EmailNotificationDTO emailNotificationDTO;

  @BeforeEach
  void setUp() {
    emailNotificationDTO =
        new EmailNotificationDTO("<html><body>Hello!</body></html>", "test@example.com");
    validJsonMessage =
        "{\"content\":\"<html><body>Hello!</body></html>\",\"recipientEmail\":\"test@example.com\"}";
  }

  @Test
  @DisplayName("Should deserialize message and send email successfully")
  void shouldDeserializeMessageAndSendEmailSuccessfully() throws JsonProcessingException {
    when(objectMapper.readValue(validJsonMessage, EmailNotificationDTO.class))
        .thenReturn(emailNotificationDTO);

    sendEmailNotificationConsumer.receiveMessage(validJsonMessage);

    verify(objectMapper, times(1)).readValue(validJsonMessage, EmailNotificationDTO.class);
    verify(mailServiceAdapter, times(1))
        .sendEmail(emailNotificationDTO.content(), emailNotificationDTO.recipientEmail());
  }

  @Test
  @DisplayName("Should throw JsonProcessingException when message is invalid")
  void shouldThrowJsonProcessingExceptionWhenMessageIsInvalid() throws JsonProcessingException {
    var invalidJsonMessage = "{invalid json}";
    doThrow(JsonProcessingException.class)
        .when(objectMapper)
        .readValue(invalidJsonMessage, EmailNotificationDTO.class);

    assertThrows(
        JsonProcessingException.class,
        () -> sendEmailNotificationConsumer.receiveMessage(invalidJsonMessage));

    verify(objectMapper, times(1)).readValue(invalidJsonMessage, EmailNotificationDTO.class);
    verify(mailServiceAdapter, never()).sendEmail(any(), any());
  }

  @Test
  @DisplayName("Should handle empty content in email notification")
  void shouldHandleEmptyContentInEmailNotification() throws JsonProcessingException {
    var emptyContentDTO = new EmailNotificationDTO("", "empty@example.com");
    var emptyContentJson = "{\"content\":\"\",\"recipientEmail\":\"empty@example.com\"}";

    when(objectMapper.readValue(emptyContentJson, EmailNotificationDTO.class))
        .thenReturn(emptyContentDTO);

    sendEmailNotificationConsumer.receiveMessage(emptyContentJson);

    verify(objectMapper, times(1)).readValue(emptyContentJson, EmailNotificationDTO.class);
    verify(mailServiceAdapter, times(1))
        .sendEmail(emptyContentDTO.content(), emptyContentDTO.recipientEmail());
  }

  @Test
  @DisplayName("Should handle different recipient email")
  void shouldHandleDifferentRecipientEmail() throws JsonProcessingException {
    var differentRecipientDTO = new EmailNotificationDTO("Content", "another@domain.com");
    var differentRecipientJson =
        "{\"content\":\"Content\",\"recipientEmail\":\"another@domain.com\"}";

    when(objectMapper.readValue(differentRecipientJson, EmailNotificationDTO.class))
        .thenReturn(differentRecipientDTO);

    sendEmailNotificationConsumer.receiveMessage(differentRecipientJson);

    verify(objectMapper, times(1)).readValue(differentRecipientJson, EmailNotificationDTO.class);
    verify(mailServiceAdapter, times(1))
        .sendEmail(differentRecipientDTO.content(), differentRecipientDTO.recipientEmail());
  }
}

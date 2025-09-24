package br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmailNotificationProducerImplTest {

  @Mock private SqsTemplate sqsTemplate;

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private EmailNotificationProducerImpl emailNotificationProducer;

  private final String testQueueName =
      "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/sendmail-queue";
  private EmailNotificationDTO testDto;
  private String testJsonContent;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(emailNotificationProducer, "sendMailQueue", testQueueName);
    testDto = new EmailNotificationDTO("Test Content", "test@example.com");
    testJsonContent = "{\"content\":\"Test Content\",\"recipientEmail\":\"test@example.com\"}";
  }

  @Test
  @DisplayName("Should send email notification successfully")
  void shouldSendEmailNotificationSuccessfully() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(testDto)).thenReturn(testJsonContent);

    when(sqsTemplate.send(testQueueName, testJsonContent)).thenReturn(mock(SendResult.class));

    emailNotificationProducer.sendToEmailNotification(testDto);

    verify(objectMapper, times(1)).writeValueAsString(testDto);
    verify(sqsTemplate, times(1)).send(testQueueName, testJsonContent);
  }

  @Test
  @DisplayName("Should handle JsonProcessingException during serialization")
  void shouldHandleJsonProcessingExceptionDuringSerialization() throws JsonProcessingException {
    doThrow(new JsonProcessingException("Serialization error") {})
        .when(objectMapper)
        .writeValueAsString(testDto);

    emailNotificationProducer.sendToEmailNotification(testDto);

    verify(objectMapper, times(1)).writeValueAsString(testDto);
    verify(sqsTemplate, times(0)).send(any(String.class), any(String.class));
  }
}

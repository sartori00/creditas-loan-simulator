package br.com.creditas.loansimulator.infrastructure.gateway.email.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class MailServiceAdapterImplTest {

  @Mock private JavaMailSender javaMailSender;

  @InjectMocks private MailServiceAdapterImpl mailServiceAdapter;

  private String content;
  private String recipientEmail;
  private MimeMessage mimeMessage;

  @BeforeEach
  void setUp() {
    content = "<html><body><h1>Hello!</h1><p>Your loan simulation is ready.</p></body></html>";
    recipientEmail = "test@example.com";
    mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
  }

  @Test
  @DisplayName("Should send email successfully")
  void shouldSendEmailSuccessfully() {
    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Should handle MailAuthenticationException")
  void shouldHandleMailAuthenticationException() {
    doThrow(new MailAuthenticationException("Authentication failed"))
        .when(javaMailSender)
        .send(any(MimeMessage.class));

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Should handle MailException during message creation")
  void shouldHandleMailExceptionDuringMessageCreation() {
    when(javaMailSender.createMimeMessage())
        .thenThrow(new MailSendException("Error creating message"));

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  @DisplayName("Should handle MessagingException when setting from address")
  void shouldHandleMessagingExceptionWhenSettingFromAddress() throws MessagingException {
    doThrow(new MessagingException("Error setting from"))
        .when(mimeMessage)
        .setFrom(any(Address.class));

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(mimeMessage, times(1)).setFrom(any(Address.class));
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  @DisplayName("Should handle MessagingException when setting subject")
  void shouldHandleMessagingExceptionWhenSettingSubject() throws MessagingException {
    doThrow(new MessagingException("Error setting subject"))
        .when(mimeMessage)
        .setSubject(anyString(), anyString());

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(mimeMessage, times(1)).setSubject(anyString(), anyString());
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  @DisplayName("Should handle MessagingException when setting text content")
  void shouldHandleMessagingExceptionWhenSettingTextContent() throws MessagingException {
    doThrow(new MessagingException("Error setting content"))
        .when(mimeMessage)
        .setContent(any(), anyString());

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(0)).send(any(MimeMessage.class));
  }

  @Test
  @DisplayName("Should handle generic MailException during email sending")
  void shouldHandleGenericMailExceptionDuringSending() {
    doThrow(new MailSendException("Generic mail error"))
        .when(javaMailSender)
        .send(any(MimeMessage.class));

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Should handle unexpected Exception")
  void shouldHandleUnexpectedException() {
    doThrow(new RuntimeException("Unexpected error"))
        .when(javaMailSender)
        .send(any(MimeMessage.class));

    mailServiceAdapter.sendEmail(content, recipientEmail);

    verify(javaMailSender, times(1)).createMimeMessage();
    verify(javaMailSender, times(1)).send(mimeMessage);
  }
}

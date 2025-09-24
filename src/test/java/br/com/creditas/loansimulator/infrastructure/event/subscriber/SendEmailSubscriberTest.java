package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.EmailNotificationProducer;
import br.com.creditas.loansimulator.infrastructure.gateway.producer.notification.impl.dto.EmailNotificationDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendEmailSubscriberTest {

  @Mock private EmailNotificationProducer emailNotificationProducer;

  @InjectMocks private SendEmailSubscriber sendEmailSubscriber;

  private Person person;
  private LoanSimulation loanSimulation;
  private NewLoanCalculatedObservable event;

  @BeforeEach
  void setUp() {
    person =
        new Person(UUID.randomUUID(), "12345678900", LocalDate.of(1990, 1, 1), "test@example.com");

    loanSimulation = new LoanSimulation(Currency.BRL, new BigDecimal("10000.00"), 12, person);
    loanSimulation.setInstallmentAmount(new BigDecimal("900.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));
    loanSimulation.setInstallmentAmount(new BigDecimal("900.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));

    event = new NewLoanCalculatedObservable(this, loanSimulation);
  }

  @Test
  @DisplayName("Should send email notification with correct content and recipient")
  void shouldSendEmailNotificationWithCorrectContentAndRecipient() {
    ArgumentCaptor<EmailNotificationDTO> emailNotificationDTOCaptor =
        ArgumentCaptor.forClass(EmailNotificationDTO.class);

    sendEmailSubscriber.onApplicationEvent(event);

    verify(emailNotificationProducer, times(1))
        .sendToEmailNotification(emailNotificationDTOCaptor.capture());

    var capturedDTO = emailNotificationDTOCaptor.getValue();

    var expectedRecipientEmail = person.getEmail();
    var expectedContent =
        String.format(
            "<html><body>"
                + "<h2>Olá %s,</h2>"
                + "<p>Sua simulação de empréstimo foi calculada com sucesso!</p>"
                + "<p>Detalhes da simulação:</p>"
                + "<ul>"
                + "<li>Valor solicitado para Empréstimo: %s %s</li>"
                + "<li>Número de Parcelas: %d</li>"
                + "<li>Valor das Parcelas mensais: %s %s</li>"
                + "<li>Valor total a ser pago: %s %s</li>"
                + "<li>Valor total de juros a ser pago: %s %s</li>"
                + "</ul>"
                + "<p>Obrigado por usar nosso simulador!</p>"
                + "</body></html>",
            loanSimulation.getPerson().getEmail(),
            loanSimulation.getCurrency().getSymbol(),
            loanSimulation.getLoanAmount(),
            loanSimulation.getQtInstallments(),
            Currency.BRL.getSymbol(),
            loanSimulation.getInstallmentAmount(),
            Currency.BRL.getSymbol(),
            loanSimulation.getTotalAmountToPay().setScale(2, RoundingMode.UP),
            Currency.BRL.getSymbol(),
            loanSimulation.getTotalInterest().setScale(2, RoundingMode.UP));

    assertEquals(expectedRecipientEmail, capturedDTO.recipientEmail());
    assertEquals(expectedContent, capturedDTO.content());
  }

  @Test
  @DisplayName("Should return true for supportsAsyncExecution")
  void shouldReturnTrueForSupportsAsyncExecution() {
    assertTrue(sendEmailSubscriber.supportsAsyncExecution());
  }
}

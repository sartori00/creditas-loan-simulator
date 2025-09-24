package br.com.creditas.loansimulator.infrastructure.event.publisher.impl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import java.math.BigDecimal;
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
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class EventPublisherImplTest {

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private EventPublisherImpl eventPublisher;

  private LoanSimulation loanSimulation;
  private Object source;

  @BeforeEach
  void setUp() {
    var person =
        new Person(UUID.randomUUID(), "12345678900", LocalDate.of(1990, 1, 1), "test@example.com");

    loanSimulation = new LoanSimulation(Currency.BRL, new BigDecimal("10000.00"), 12, person);
    loanSimulation.setInstallmentAmount(new BigDecimal("900.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));

    source = new Object();
  }

  @Test
  @DisplayName("Should publish NewLoanCalculatedObservable event with correct loan simulation")
  void shouldPublishNewLoanCalculatedObservableEventWithCorrectLoanSimulation() {
    ArgumentCaptor<NewLoanCalculatedObservable> eventCaptor =
        ArgumentCaptor.forClass(NewLoanCalculatedObservable.class);

    eventPublisher.publishEvent(source, loanSimulation);

    verify(applicationEventPublisher, times(1)).publishEvent(eventCaptor.capture());

    var capturedEvent = eventCaptor.getValue();
    assertNotNull(capturedEvent);
    assertSame(loanSimulation, capturedEvent.getLoanSimulation());
    assertInstanceOf(EventPublisherImpl.class, capturedEvent.getSource());
  }

  @Test
  @DisplayName("Should publish event even if loan simulation details are minimal")
  void shouldPublishEventEvenIfLoanSimulationDetailsAreMinimal() {
    var minimalLoanSimulation =
        new LoanSimulation(
            Currency.USD,
            new BigDecimal("500.00"),
            6,
            new Person(
                UUID.randomUUID(),
                "11122233344",
                LocalDate.of(1985, 5, 10),
                "another@example.com"));
    minimalLoanSimulation.setInstallmentAmount(new BigDecimal("100.00"));
    minimalLoanSimulation.setLoanAmountBRL(new BigDecimal("500.00"));

    ArgumentCaptor<NewLoanCalculatedObservable> eventCaptor =
        ArgumentCaptor.forClass(NewLoanCalculatedObservable.class);

    eventPublisher.publishEvent(source, minimalLoanSimulation);

    verify(applicationEventPublisher, times(1)).publishEvent(eventCaptor.capture());

    var capturedEvent = eventCaptor.getValue();
    assertNotNull(capturedEvent);
    assertSame(minimalLoanSimulation, capturedEvent.getLoanSimulation());
  }
}

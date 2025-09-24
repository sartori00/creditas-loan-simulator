package br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.application.usecase.LoanMultipleSimulatorUseCase;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationRequestDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.LoanSimulationResponseDto;
import br.com.creditas.loansimulator.infrastructure.entrypoint.controller.loan.dto.PersonRequestDto;
import br.com.creditas.loansimulator.infrastructure.event.publisher.EventPublisher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

  @Mock private LoanSimulatorUseCase loanSimulatorUseCase;

  @Mock private LoanMultipleSimulatorUseCase loanMultipleSimulatorUseCase;

  @Mock private EventPublisher eventPublisher;

  @InjectMocks private LoanController loanController;

  private LoanSimulationRequestDto loanSimulationRequestDto;
  private LoanSimulation loanSimulation;

  @BeforeEach
  void setUp() {
    var personRequestDto =
        new PersonRequestDto("12345678900", LocalDate.of(1990, 1, 1), "test@example.com");
    loanSimulationRequestDto =
        new LoanSimulationRequestDto(
            Currency.BRL, new BigDecimal("10000.00"), 24, personRequestDto);

    var person = new Person("12345678900", LocalDate.of(1990, 1, 1), "test@example.com");
    loanSimulation =
        new LoanSimulation(
            UUID.randomUUID(),
            person,
            Currency.BRL,
            new BigDecimal("10000.00"),
            new BigDecimal("10000.00"),
            24,
            new BigDecimal("500.00"));
    loanSimulation.setInstallmentAmount(new BigDecimal("500.00"));
    loanSimulation.setLoanAmountBRL(new BigDecimal("10000.00"));
  }

  @Test
  @DisplayName("Should simulate a loan and publish an event successfully")
  void shouldSimulateALoanAndPublishAnEventSuccessfully() {
    when(loanSimulatorUseCase.execute(any(LoanSimulation.class))).thenReturn(loanSimulation);

    ResponseEntity<LoanSimulationResponseDto> response =
        loanController.simulateALoan(loanSimulationRequestDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(loanSimulation.getTotalAmountToPay(), response.getBody().totalAmountToPay());
    verify(loanSimulatorUseCase, times(1)).execute(any(LoanSimulation.class));
    verify(eventPublisher, times(1)).publishEvent(loanController, loanSimulation);
  }

  @Test
  @DisplayName("Should simulate multiple loans and publish events successfully")
  void shouldSimulateMultipleLoansAndPublishEventsSuccessfully()
      throws ExecutionException, InterruptedException, TimeoutException {
    List<LoanSimulationRequestDto> requestDtos =
        List.of(loanSimulationRequestDto, loanSimulationRequestDto);
    List<LoanSimulation> simulatedLoans = List.of(loanSimulation, loanSimulation);

    when(loanMultipleSimulatorUseCase.execute(any(List.class)))
        .thenReturn(CompletableFuture.completedFuture(simulatedLoans));

    CompletableFuture<ResponseEntity<List<LoanSimulationResponseDto>>> futureResponse =
        loanController.simulateMultipleLoans(requestDtos);

    ResponseEntity<List<LoanSimulationResponseDto>> response =
        futureResponse.get(5, TimeUnit.SECONDS);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    verify(loanMultipleSimulatorUseCase, times(1)).execute(any(List.class));
  }

  @Test
  @DisplayName("Should return empty list when no DTOs are provided for multiple simulations")
  void shouldReturnEmptyListWhenNoDtosAreProvidedForMultipleSimulations()
      throws ExecutionException, InterruptedException, TimeoutException {
    List<LoanSimulationRequestDto> emptyRequestDtos = Collections.emptyList();
    List<LoanSimulation> emptySimulatedLoans = Collections.emptyList();

    when(loanMultipleSimulatorUseCase.execute(any(List.class)))
        .thenReturn(CompletableFuture.completedFuture(emptySimulatedLoans));

    CompletableFuture<ResponseEntity<List<LoanSimulationResponseDto>>> futureResponse =
        loanController.simulateMultipleLoans(emptyRequestDtos);

    ResponseEntity<List<LoanSimulationResponseDto>> response =
        futureResponse.get(5, TimeUnit.SECONDS);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
    verify(loanMultipleSimulatorUseCase, times(1)).execute(any(List.class));

    verify(eventPublisher, times(0)).publishEvent(any(), any());
  }
}

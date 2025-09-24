package br.com.creditas.loansimulator.application.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.application.exceptions.BusinessException;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanMultipleSimulatorUseCaseImplTest {

  @Mock private LoanSimulatorUseCase loanSimulatorUseCase;

  @Mock private Executor batchModeTaskExecutor;

  @InjectMocks private LoanMultipleSimulatorUseCaseImpl loanMultipleSimulatorUseCase;

  private Person person;

  @BeforeEach
  void setUp() {
    person = new Person("12345678900", LocalDate.of(1990, 1, 1), "test@example.com");
  }

  @Test
  @DisplayName("Should return empty list when simulations list is empty")
  void shouldReturnEmptyListWhenSimulationsListIsEmpty()
      throws ExecutionException, InterruptedException {
    List<LoanSimulation> simulationsList = Collections.emptyList();

    CompletableFuture<List<LoanSimulation>> resultFuture =
        loanMultipleSimulatorUseCase.execute(simulationsList);

    assertTrue(resultFuture.isDone());
    assertTrue(resultFuture.get().isEmpty());
    verify(loanSimulatorUseCase, times(0)).execute(any(LoanSimulation.class));
  }

  @Test
  @DisplayName("Should successfully simulate multiple loans")
  void shouldSuccessfullySimulateMultipleLoans() throws ExecutionException, InterruptedException {
    this.setupRunnable();

    var simulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    var simulation2 = new LoanSimulation(Currency.BRL, new BigDecimal("5000"), 24, person);

    var resultSimulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    resultSimulation1.setLoanAmountBRL(new BigDecimal("5000.00"));
    resultSimulation1.setInstallmentAmount(new BigDecimal("450.00"));

    var resultSimulation2 = new LoanSimulation(Currency.BRL, new BigDecimal("5000"), 24, person);
    resultSimulation2.setLoanAmountBRL(new BigDecimal("5000.00"));
    resultSimulation2.setInstallmentAmount(new BigDecimal("250.00"));

    when(loanSimulatorUseCase.execute(simulation1)).thenReturn(resultSimulation1);
    when(loanSimulatorUseCase.execute(simulation2)).thenReturn(resultSimulation2);

    List<LoanSimulation> simulationsList = Arrays.asList(simulation1, simulation2);

    CompletableFuture<List<LoanSimulation>> resultFuture =
        loanMultipleSimulatorUseCase.execute(simulationsList);

    assertTrue(resultFuture.isDone());
    List<LoanSimulation> results = resultFuture.get();

    assertNotNull(results);
    assertEquals(2, results.size());
    assertEquals(resultSimulation1, results.get(0));
    assertEquals(resultSimulation2, results.get(1));

    verify(loanSimulatorUseCase, times(1)).execute(simulation1);
    verify(loanSimulatorUseCase, times(1)).execute(simulation2);
  }

  @Test
  @DisplayName("Should handle exception when one simulation fails")
  void shouldHandleExceptionWhenOneSimulationFails() {
    this.setupRunnable();

    var simulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    var simulation2 = new LoanSimulation(Currency.BRL, new BigDecimal("5000"), 24, person);

    var resultSimulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    resultSimulation1.setLoanAmountBRL(new BigDecimal("5000.00"));
    resultSimulation1.setInstallmentAmount(new BigDecimal("450.00"));

    when(loanSimulatorUseCase.execute(simulation1)).thenReturn(resultSimulation1);
    when(loanSimulatorUseCase.execute(simulation2))
        .thenThrow(new BusinessException("Simulation failed for loan 2"));

    List<LoanSimulation> simulationsList = Arrays.asList(simulation1, simulation2);

    CompletableFuture<List<LoanSimulation>> resultFuture =
        loanMultipleSimulatorUseCase.execute(simulationsList);

    assertTrue(resultFuture.isCompletedExceptionally());

    var exception = assertThrows(ExecutionException.class, resultFuture::get);
    assertInstanceOf(BusinessException.class, exception.getCause());
    assertEquals("Simulation failed for loan 2", exception.getCause().getMessage());

    verify(loanSimulatorUseCase, times(1)).execute(simulation1);
    verify(loanSimulatorUseCase, times(1)).execute(simulation2);
  }

  @Test
  @DisplayName("Should use the provided executor for async tasks")
  void shouldUseTheProvidedExecutorForAsyncTasks() throws ExecutionException, InterruptedException {
    var realExecutor = Executors.newSingleThreadExecutor();

    loanMultipleSimulatorUseCase =
        new LoanMultipleSimulatorUseCaseImpl(loanSimulatorUseCase, realExecutor);

    var simulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    var resultSimulation1 = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    resultSimulation1.setLoanAmountBRL(new BigDecimal("5000.00"));
    resultSimulation1.setInstallmentAmount(new BigDecimal("450.00"));

    when(loanSimulatorUseCase.execute(simulation1)).thenReturn(resultSimulation1);

    List<LoanSimulation> simulationsList = Collections.singletonList(simulation1);

    CompletableFuture<List<LoanSimulation>> resultFuture =
        loanMultipleSimulatorUseCase.execute(simulationsList);

    List<LoanSimulation> results = resultFuture.get();

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(resultSimulation1, results.getFirst());

    verify(loanSimulatorUseCase, times(1)).execute(simulation1);
  }

  private void setupRunnable() {
    doAnswer(
            invocation -> {
              Runnable runnable = invocation.getArgument(0);
              runnable.run();
              return null;
            })
        .when(batchModeTaskExecutor)
        .execute(any(Runnable.class));
  }
}

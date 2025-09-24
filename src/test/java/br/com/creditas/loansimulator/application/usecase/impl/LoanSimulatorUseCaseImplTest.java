package br.com.creditas.loansimulator.application.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.application.exceptions.BusinessException;
import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.Person;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.domain.service.FixedPaymentCalculator;
import br.com.creditas.loansimulator.domain.strategy.RangesStrategy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanSimulatorUseCaseImplTest {

  @Mock private ExchangeRateService exchangeRateService;

  @Mock private FixedPaymentCalculator fixedPaymentCalculator;

  @Mock private List<RangesStrategy> rangesStrategies;

  @InjectMocks private LoanSimulatorUseCaseImpl loanSimulatorUseCase;

  private LoanSimulation loanSimulation;
  private Person person;

  @BeforeEach
  void setUp() {
    person = new Person("12345678900", LocalDate.now().minusYears(30), "test@example.com");
    loanSimulation = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
  }

  @Test
  @DisplayName("Should successfully execute loan simulation for USD currency")
  void shouldSuccessfullyExecuteLoanSimulationForUSDCurrency() {
    loanSimulation = new LoanSimulation(Currency.USD, new BigDecimal("1000"), 12, person);
    var monthlyRate = new BigDecimal("0.02");
    var installmentAmount = new BigDecimal("90.00");
    var expectedLoanAmountBRL = new BigDecimal("5270.00");

    when(exchangeRateService.getRateToBRL(Currency.USD)).thenReturn(new BigDecimal("5.27"));

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(monthlyRate, expectedLoanAmountBRL, 12))
        .thenReturn(installmentAmount);

    var result = loanSimulatorUseCase.execute(loanSimulation);

    assertNotNull(result);
    assertEquals(expectedLoanAmountBRL, result.getLoanAmountBRL());
    assertEquals(installmentAmount, result.getInstallmentAmount());
  }

  @Test
  @DisplayName("Should successfully execute loan simulation for BRL currency")
  void shouldSuccessfullyExecuteLoanSimulationForBRLCurrency() {
    loanSimulation = new LoanSimulation(Currency.BRL, new BigDecimal("1000"), 12, person);
    var monthlyRate = new BigDecimal("0.02");
    var installmentAmount = new BigDecimal("90.00");

    when(exchangeRateService.getRateToBRL(Currency.BRL)).thenReturn(BigDecimal.ONE);

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(monthlyRate, new BigDecimal("1000"), 12))
        .thenReturn(installmentAmount);

    var result = loanSimulatorUseCase.execute(loanSimulation);

    assertNotNull(result);
    assertEquals(new BigDecimal("1000"), result.getLoanAmountBRL());
    assertEquals(installmentAmount, result.getInstallmentAmount());
  }

  @Test
  @DisplayName("Should successfully execute loan simulation for EUR currency")
  void shouldSuccessfullyExecuteLoanSimulationForEURCurrency() {
    loanSimulation = new LoanSimulation(Currency.EUR, new BigDecimal("1000"), 12, person);
    var monthlyRate = new BigDecimal("0.02");
    var installmentAmount = new BigDecimal("90.00");
    var expectedLoanAmountBRL = new BigDecimal("6220.00");

    when(exchangeRateService.getRateToBRL(Currency.EUR)).thenReturn(new BigDecimal("6.22"));

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(monthlyRate, expectedLoanAmountBRL, 12))
        .thenReturn(installmentAmount);

    var result = loanSimulatorUseCase.execute(loanSimulation);

    assertNotNull(result);
    assertEquals(expectedLoanAmountBRL, result.getLoanAmountBRL());
    assertEquals(installmentAmount, result.getInstallmentAmount());
  }

  @Test
  @DisplayName("Should throw BusinessException when age is unsupported")
  void shouldThrowBusinessExceptionWhenAgeIsUnsupported() {
    when(exchangeRateService.getRateToBRL(any(Currency.class))).thenReturn(new BigDecimal("5.0"));

    var mockStrategy = mock(RangesStrategy.class);

    when(mockStrategy.isThisRange(anyInt())).thenReturn(false);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    assertThrows(BusinessException.class, () -> loanSimulatorUseCase.execute(loanSimulation));
  }

  @Test
  @DisplayName("Should successfully execute loan simulation for GBP currency")
  void shouldSuccessfullyExecuteLoanSimulationForGBPCurrency() {
    loanSimulation = new LoanSimulation(Currency.GBP, new BigDecimal("1000"), 12, person);
    var monthlyRate = new BigDecimal("0.02");
    var installmentAmount = new BigDecimal("90.00");
    var expectedLoanAmountBRL = new BigDecimal("7120.00");

    when(exchangeRateService.getRateToBRL(Currency.GBP)).thenReturn(new BigDecimal("7.12"));

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(monthlyRate, expectedLoanAmountBRL, 12))
        .thenReturn(installmentAmount);

    var result = loanSimulatorUseCase.execute(loanSimulation);

    assertNotNull(result);
    assertEquals(expectedLoanAmountBRL, result.getLoanAmountBRL());
    assertEquals(installmentAmount, result.getInstallmentAmount());
  }

  @Test
  @DisplayName("Should successfully execute loan simulation for CNY currency")
  void shouldSuccessfullyExecuteLoanSimulationForCNYCurrency() {
    loanSimulation = new LoanSimulation(Currency.CNY, new BigDecimal("1000"), 12, person);
    var monthlyRate = new BigDecimal("0.02");
    var installmentAmount = new BigDecimal("90.00");
    var expectedLoanAmountBRL = new BigDecimal("740.00");

    when(exchangeRateService.getRateToBRL(Currency.CNY)).thenReturn(new BigDecimal("0.74"));

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(monthlyRate, expectedLoanAmountBRL, 12))
        .thenReturn(installmentAmount);

    var result = loanSimulatorUseCase.execute(loanSimulation);

    assertNotNull(result);
    assertEquals(expectedLoanAmountBRL, result.getLoanAmountBRL());
    assertEquals(installmentAmount, result.getInstallmentAmount());
  }

  @Test
  @DisplayName("Should throw BusinessException when fixed payment calculation fails")
  void shouldThrowBusinessExceptionWhenFixedPaymentCalculationFails() {
    var exchangeRate = new BigDecimal("5.0");
    var monthlyRate = new BigDecimal("0.01");

    when(exchangeRateService.getRateToBRL(Currency.USD)).thenReturn(exchangeRate);

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(any(BigDecimal.class), any(BigDecimal.class), anyInt()))
        .thenThrow(new IllegalArgumentException("Invalid loan amount"));

    var exception =
        assertThrows(BusinessException.class, () -> loanSimulatorUseCase.execute(loanSimulation));
    assertEquals("Invalid loan amount", exception.getMessage());
  }

  @Test
  @DisplayName(
      "Should handle negative loan amount by FixedPaymentCalculator throwing IllegalArgumentException")
  void shouldHandleNegativeLoanAmountByFixedPaymentCalculatorThrowingIllegalArgumentException() {
    loanSimulation = new LoanSimulation(Currency.USD, new BigDecimal("-1000"), 12, person);
    var exchangeRate = new BigDecimal("5.0");
    var monthlyRate = new BigDecimal("0.01");

    when(exchangeRateService.getRateToBRL(Currency.USD)).thenReturn(exchangeRate);

    var mockStrategy = mock(RangesStrategy.class);
    when(mockStrategy.isThisRange(anyInt())).thenReturn(true);
    when(mockStrategy.monthlyRateCalculation()).thenReturn(monthlyRate);
    when(rangesStrategies.stream()).thenReturn(Stream.of(mockStrategy));

    when(fixedPaymentCalculator.calculate(any(BigDecimal.class), any(BigDecimal.class), anyInt()))
        .thenThrow(new IllegalArgumentException("Loan amount cannot be negative"));

    var exception =
        assertThrows(BusinessException.class, () -> loanSimulatorUseCase.execute(loanSimulation));
    assertEquals("Loan amount cannot be negative", exception.getMessage());
  }
}

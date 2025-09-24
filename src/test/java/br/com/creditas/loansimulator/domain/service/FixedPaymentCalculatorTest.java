package br.com.creditas.loansimulator.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FixedPaymentCalculatorTest {

  private FixedPaymentCalculator calculator;

  @BeforeEach
  void setUp() {
    calculator = new FixedPaymentCalculator();
  }

  @Test
  @DisplayName(
      "Should calculate fixed payment correctly for a standard scenario - Monthly Rate = 2")
  void shouldCalculateFixedPaymentCorrectlyForStandardScenarioMonthlyRate2() {
    var monthlyRate = new BigDecimal("2");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;
    var expectedPayment = new BigDecimal("945.60");

    var actualPayment = calculator.calculate(monthlyRate, valueForLoan, installments);

    assertNotNull(actualPayment);
    assertEquals(expectedPayment, actualPayment);
  }

  @Test
  @DisplayName(
      "Should calculate fixed payment correctly for a standard scenario - Monthly Rate = 3")
  void shouldCalculateFixedPaymentCorrectlyForStandardScenarioMonthlyRate3() {
    var monthlyRate = new BigDecimal("3");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;
    var expectedPayment = new BigDecimal("1004.62");

    var actualPayment = calculator.calculate(monthlyRate, valueForLoan, installments);

    assertNotNull(actualPayment);
    assertEquals(expectedPayment, actualPayment);
  }

  @Test
  @DisplayName(
      "Should calculate fixed payment correctly for a standard scenario - Monthly Rate = 4")
  void shouldCalculateFixedPaymentCorrectlyForStandardScenarioMonthlyRate4() {
    var monthlyRate = new BigDecimal("4");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;
    var expectedPayment = new BigDecimal("1065.52");

    var actualPayment = calculator.calculate(monthlyRate, valueForLoan, installments);

    assertNotNull(actualPayment);
    assertEquals(expectedPayment, actualPayment);
  }

  @Test
  @DisplayName(
      "Should calculate fixed payment correctly for a standard scenario - Monthly Rate = 5")
  void shouldCalculateFixedPaymentCorrectlyForStandardScenarioMonthlyRate5() {
    var monthlyRate = new BigDecimal("5");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;
    var expectedPayment = new BigDecimal("1128.25");

    var actualPayment = calculator.calculate(monthlyRate, valueForLoan, installments);

    assertNotNull(actualPayment);
    assertEquals(expectedPayment, actualPayment);
  }

  @Test
  @DisplayName("Should calculate fixed payment correctly for one installment")
  void shouldCalculateFixedPaymentCorrectlyForOneInstallment() {
    var monthlyRate = new BigDecimal("2.0");
    var valueForLoan = new BigDecimal("1000.00");
    var installments = 1;

    var expectedPayment = new BigDecimal("1020.00");

    var actualPayment = calculator.calculate(monthlyRate, valueForLoan, installments);

    assertNotNull(actualPayment);
    assertEquals(expectedPayment, actualPayment);
  }

  @Test
  @DisplayName("Should throw ArithmeticException when monthly rate is zero due to division by zero")
  void shouldThrowArithmeticExceptionWhenMonthlyRateIsZero() {
    var monthlyRate = BigDecimal.ZERO;
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 10;

    assertThrows(
        ArithmeticException.class,
        () -> calculator.calculate(monthlyRate, valueForLoan, installments));
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when loan value is null")
  void shouldThrowExceptionWhenLoanValueIsNull() {
    var monthlyRate = new BigDecimal("1.0");
    var installments = 12;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, null, installments));
    assertEquals("The loan value must be greater than zero.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when loan value is zero")
  void shouldThrowExceptionWhenLoanValueIsZero() {
    var monthlyRate = new BigDecimal("1.0");
    var valueForLoan = BigDecimal.ZERO;
    var installments = 12;

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, valueForLoan, installments));
    assertEquals("The loan value must be greater than zero.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when loan value is negative")
  void shouldThrowExceptionWhenLoanValueIsNegative() {
    var monthlyRate = new BigDecimal("1.0");
    var valueForLoan = new BigDecimal("-1000.00");
    var installments = 12;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, valueForLoan, installments));
    assertEquals("The loan value must be greater than zero.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when monthly rate is null")
  void shouldThrowExceptionWhenMonthlyRateIsNull() {
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(null, valueForLoan, installments));
    assertEquals("The monthly interest rate cannot be negative.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when monthly rate is negative")
  void shouldThrowExceptionWhenMonthlyRateIsNegative() {
    var monthlyRate = new BigDecimal("-0.5");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 12;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, valueForLoan, installments));
    assertEquals("The monthly interest rate cannot be negative.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when installments are zero")
  void shouldThrowExceptionWhenInstallmentsAreZero() {
    var monthlyRate = new BigDecimal("1.0");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = 0;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, valueForLoan, installments));
    assertEquals("The number of installments must be greater than zero.", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when installments are negative")
  void shouldThrowExceptionWhenInstallmentsAreNegative() {
    var monthlyRate = new BigDecimal("1.0");
    var valueForLoan = new BigDecimal("10000.00");
    var installments = -5;

    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> calculator.calculate(monthlyRate, valueForLoan, installments));
    assertEquals("The number of installments must be greater than zero.", exception.getMessage());
  }
}

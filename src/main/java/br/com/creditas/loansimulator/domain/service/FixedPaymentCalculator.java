package br.com.creditas.loansimulator.domain.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class FixedPaymentCalculator {

  private static final int FINANCIAL_SCALE = 2;
  private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  public BigDecimal calculate(BigDecimal monthlyRate, BigDecimal valueForLoan, int installments)
      throws IllegalArgumentException {
    this.validateParams(monthlyRate, valueForLoan, installments);

    var monthlyRateDecimal = this.transformRateInDecimal(monthlyRate);

    var numerator = this.calculateNumerator(valueForLoan, monthlyRateDecimal);

    var denominator = this.calculateDenominator(monthlyRateDecimal, installments);

    return this.resolveFraction(numerator, denominator);
  }

  private void validateParams(BigDecimal monthlyRate, BigDecimal valueForLoan, int installments)
      throws IllegalArgumentException {
    if (valueForLoan == null || valueForLoan.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("The loan value must be greater than zero.");
    }
    if (monthlyRate == null || monthlyRate.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("The monthly interest rate cannot be negative.");
    }
    if (installments <= 0) {
      throw new IllegalArgumentException("The number of installments must be greater than zero.");
    }
  }

  private BigDecimal transformRateInDecimal(BigDecimal monthlyRate) {
    return monthlyRate.divide(new BigDecimal(100), MATH_CONTEXT);
  }

  private BigDecimal calculateNumerator(BigDecimal valueForLoan, BigDecimal monthlyRateDecimal) {
    return valueForLoan.multiply(monthlyRateDecimal, MATH_CONTEXT);
  }

  private BigDecimal calculateDenominator(BigDecimal monthlyRateDecimal, int installments) {
    var onePlusRate = BigDecimal.ONE.add(monthlyRateDecimal, MATH_CONTEXT);

    var power = BigDecimal.valueOf(Math.pow(onePlusRate.doubleValue(), -installments));

    return BigDecimal.ONE.subtract(power);
  }

  private BigDecimal resolveFraction(BigDecimal numerator, BigDecimal denominator) {
    return numerator.divide(denominator, FINANCIAL_SCALE, ROUNDING_MODE);
  }
}

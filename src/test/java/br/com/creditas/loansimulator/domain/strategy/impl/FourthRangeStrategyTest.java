package br.com.creditas.loansimulator.domain.strategy.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;
import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FourthRangeStrategyTest {

  @Mock private RateRanges rateRanges;

  private FourthRangeStrategy fourthRangeStrategy;

  @BeforeEach
  void setUp() {
    when(rateRanges.getFourthRateRange()).thenReturn(BigDecimal.valueOf(4));
    fourthRangeStrategy = new FourthRangeStrategy(rateRanges);
  }

  @Test
  @DisplayName("Should return true for age greater than or equal to RANGE_BEGINNING")
  void shouldReturnTrueForAgeGreaterThanOrEqualToRangeBeginning() {
    var ageAtBeginning = 61;
    var ageAboveBeginning = 70;

    var resultAtBeginning = fourthRangeStrategy.isThisRange(ageAtBeginning);
    var resultAboveBeginning = fourthRangeStrategy.isThisRange(ageAboveBeginning);

    assertTrue(resultAtBeginning);
    assertTrue(resultAboveBeginning);
  }

  @Test
  @DisplayName("Should return false for age below RANGE_BEGINNING")
  void shouldReturnFalseForAgeBelowRangeBeginning() {
    var ageBelowRange = 60;

    var result = fourthRangeStrategy.isThisRange(ageBelowRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should calculate monthly rate correctly based on fourthRateRange")
  void shouldCalculateMonthlyRateCorrectly() {
    var expectedRateRange = BigDecimal.valueOf(4);
    var oneYear = new BigDecimal(12);
    var expectedMonthlyRate = expectedRateRange.divide(oneYear, MathContext.DECIMAL128);

    var actualMonthlyRate = fourthRangeStrategy.monthlyRateCalculation();

    assertNotNull(actualMonthlyRate);
    assertEquals(expectedMonthlyRate, actualMonthlyRate);
    assertEquals(expectedMonthlyRate.scale(), actualMonthlyRate.scale());
  }
}

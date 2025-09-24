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
class FirstRangeStrategyTest {

  @Mock private RateRanges rateRanges;

  private FirstRangeStrategy firstRangeStrategy;

  @BeforeEach
  void setUp() {
    when(rateRanges.getFirstRateRange()).thenReturn(BigDecimal.valueOf(5));
    firstRangeStrategy = new FirstRangeStrategy(rateRanges);
  }

  @Test
  @DisplayName("Should return true for age less than or equal to RANGE_FINAL")
  void shouldReturnTrueForAgeLessThanOrEqualToRangeFinal() {
    var ageWithinRange = 25;
    var ageBelowRange = 18;

    var resultWithin = firstRangeStrategy.isThisRange(ageWithinRange);
    var resultBelow = firstRangeStrategy.isThisRange(ageBelowRange);

    assertTrue(resultWithin);
    assertTrue(resultBelow);
  }

  @Test
  @DisplayName("Should return false for age greater than RANGE_FINAL")
  void shouldReturnFalseForAgeGreaterThanRangeFinal() {
    var ageOutsideRange = 26;

    var result = firstRangeStrategy.isThisRange(ageOutsideRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should calculate monthly rate correctly based on firstRateRange")
  void shouldCalculateMonthlyRateCorrectly() {
    var expectedRateRange = BigDecimal.valueOf(5);
    var oneYear = new BigDecimal(12);

    var expectedMonthlyRate = expectedRateRange.divide(oneYear, MathContext.DECIMAL128);

    var actualMonthlyRate = firstRangeStrategy.monthlyRateCalculation();

    assertNotNull(actualMonthlyRate);
    assertEquals(expectedMonthlyRate, actualMonthlyRate);
    assertEquals(expectedMonthlyRate.scale(), actualMonthlyRate.scale());
  }
}

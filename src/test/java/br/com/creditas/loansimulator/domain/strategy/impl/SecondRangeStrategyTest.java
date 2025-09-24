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
class SecondRangeStrategyTest {

  @Mock private RateRanges rateRanges;

  private SecondRangeStrategy secondRangeStrategy;

  @BeforeEach
  void setUp() {
    when(rateRanges.getSecondRateRange()).thenReturn(BigDecimal.valueOf(3));
    secondRangeStrategy = new SecondRangeStrategy(rateRanges);
  }

  @Test
  @DisplayName("Should return true for age within the range [RANGE_BEGINNING, RANGE_FINAL]")
  void shouldReturnTrueForAgeWithinRange() {
    var ageAtBeginning = 26;
    var ageAtEnd = 40;
    var ageInMiddle = 33;

    var resultBeginning = secondRangeStrategy.isThisRange(ageAtBeginning);
    var resultEnd = secondRangeStrategy.isThisRange(ageAtEnd);
    var resultMiddle = secondRangeStrategy.isThisRange(ageInMiddle);

    assertTrue(resultBeginning);
    assertTrue(resultEnd);
    assertTrue(resultMiddle);
  }

  @Test
  @DisplayName("Should return false for age below RANGE_BEGINNING")
  void shouldReturnFalseForAgeBelowRangeBeginning() {
    var ageBelowRange = 25;

    var result = secondRangeStrategy.isThisRange(ageBelowRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should return false for age above RANGE_FINAL")
  void shouldReturnFalseForAgeAboveRangeFinal() {
    var ageAboveRange = 41;

    var result = secondRangeStrategy.isThisRange(ageAboveRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should calculate monthly rate correctly based on secondRateRange")
  void shouldCalculateMonthlyRateCorrectly() {
    var expectedRateRange = BigDecimal.valueOf(3);
    var oneYear = new BigDecimal(12);

    var expectedMonthlyRate = expectedRateRange.divide(oneYear, MathContext.DECIMAL128);

    var actualMonthlyRate = secondRangeStrategy.monthlyRateCalculation();

    assertNotNull(actualMonthlyRate);
    assertEquals(expectedMonthlyRate, actualMonthlyRate);
    assertEquals(expectedMonthlyRate.scale(), actualMonthlyRate.scale());
  }
}

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
class ThirdRangeStrategyTest {

  @Mock private RateRanges rateRanges;

  private ThirdRangeStrategy thirdRangeStrategy;

  @BeforeEach
  void setUp() {
    when(rateRanges.getThirdRateRange()).thenReturn(BigDecimal.valueOf(2));
    thirdRangeStrategy = new ThirdRangeStrategy(rateRanges);
  }

  @Test
  @DisplayName("Should return true for age within the range [RANGE_BEGINNING, RANGE_FINAL]")
  void shouldReturnTrueForAgeWithinRange() {
    var ageAtBeginning = 41;
    var ageAtEnd = 60;
    var ageInMiddle = 50;

    var resultBeginning = thirdRangeStrategy.isThisRange(ageAtBeginning);
    var resultEnd = thirdRangeStrategy.isThisRange(ageAtEnd);
    var resultMiddle = thirdRangeStrategy.isThisRange(ageInMiddle);

    assertTrue(resultBeginning);
    assertTrue(resultEnd);
    assertTrue(resultMiddle);
  }

  @Test
  @DisplayName("Should return false for age below RANGE_BEGINNING")
  void shouldReturnFalseForAgeBelowRangeBeginning() {
    var ageBelowRange = 40;

    var result = thirdRangeStrategy.isThisRange(ageBelowRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should return false for age above RANGE_FINAL")
  void shouldReturnFalseForAgeAboveRangeFinal() {
    var ageAboveRange = 61;

    var result = thirdRangeStrategy.isThisRange(ageAboveRange);

    assertFalse(result);
  }

  @Test
  @DisplayName("Should calculate monthly rate correctly based on thirdRateRange")
  void shouldCalculateMonthlyRateCorrectly() {
    var expectedRateRange = BigDecimal.valueOf(2);
    var oneYear = new BigDecimal(12);
    var expectedMonthlyRate = expectedRateRange.divide(oneYear, MathContext.DECIMAL128);

    var actualMonthlyRate = thirdRangeStrategy.monthlyRateCalculation();

    assertNotNull(actualMonthlyRate);
    assertEquals(expectedMonthlyRate, actualMonthlyRate);
    assertEquals(expectedMonthlyRate.scale(), actualMonthlyRate.scale());
  }
}

package br.com.creditas.loansimulator.infrastructure.gateway.currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.creditas.loansimulator.domain.model.enums.Currency;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExchangeRateServiceImplTest {

  private ExchangeRateServiceImpl exchangeRateService;

  @BeforeEach
  void setUp() {
    exchangeRateService = new ExchangeRateServiceImpl();
  }

  @Test
  @DisplayName("Should initialize BRL currency with a rate of one")
  void shouldInitializeBrlCurrencyWithARateOfOne() {
    var brlRate = exchangeRateService.getRateToBRL(Currency.BRL);
    assertNotNull(brlRate);
    assertEquals(BigDecimal.ONE, brlRate);
  }

  @Test
  @DisplayName("Should return the correct rate for an existing currency")
  void shouldReturnTheCorrectRateForAnExistingCurrency() {
    var usdRate = new BigDecimal("5.20");
    exchangeRateService.updateRate(Currency.USD, usdRate);

    var retrievedRate = exchangeRateService.getRateToBRL(Currency.USD);
    assertNotNull(retrievedRate);
    assertEquals(usdRate, retrievedRate);
  }

  @Test
  @DisplayName("Should return null for a non-existent currency")
  void shouldReturnNullForANonExistentCurrency() {
    var retrievedRate = exchangeRateService.getRateToBRL(Currency.EUR);
    assertNull(retrievedRate);
  }

  @Test
  @DisplayName("Should update an existing currency rate")
  void shouldUpdateAnExistingCurrencyRate() {
    var initialUsdRate = new BigDecimal("5.00");
    exchangeRateService.updateRate(Currency.USD, initialUsdRate);
    assertEquals(initialUsdRate, exchangeRateService.getRateToBRL(Currency.USD));

    var updatedUsdRate = new BigDecimal("5.15");
    exchangeRateService.updateRate(Currency.USD, updatedUsdRate);
    assertEquals(updatedUsdRate, exchangeRateService.getRateToBRL(Currency.USD));
  }

  @Test
  @DisplayName("Should add a new currency rate")
  void shouldAddNewCurrencyRate() {
    var eurRate = new BigDecimal("6.00");
    assertNull(exchangeRateService.getRateToBRL(Currency.EUR));

    exchangeRateService.updateRate(Currency.EUR, eurRate);
    assertEquals(eurRate, exchangeRateService.getRateToBRL(Currency.EUR));
  }

  @Test
  @DisplayName("Should not update rate if currency is null")
  void shouldNotUpdateRateIfCurrencyIsNull() {
    var initialUsdRate = new BigDecimal("5.00");
    exchangeRateService.updateRate(Currency.USD, initialUsdRate);

    exchangeRateService.updateRate(null, new BigDecimal("4.50"));
    assertEquals(initialUsdRate, exchangeRateService.getRateToBRL(Currency.USD));
    assertEquals(BigDecimal.ONE, exchangeRateService.getRateToBRL(Currency.BRL));
  }

  @Test
  @DisplayName("Should not update rate if rate value is null")
  void shouldNotUpdateRateIfRateValueIsNull() {
    var initialUsdRate = new BigDecimal("5.00");
    exchangeRateService.updateRate(Currency.USD, initialUsdRate);

    exchangeRateService.updateRate(Currency.USD, null);
    assertEquals(initialUsdRate, exchangeRateService.getRateToBRL(Currency.USD));
  }

  @Test
  @DisplayName("Should not update rate if both currency and rate are null")
  void shouldNotUpdateRateIfBothCurrencyAndRateAreNull() {
    var initialUsdRate = new BigDecimal("5.00");
    exchangeRateService.updateRate(Currency.USD, initialUsdRate);

    exchangeRateService.updateRate(null, null);
    assertEquals(initialUsdRate, exchangeRateService.getRateToBRL(Currency.USD));
  }
}

package br.com.creditas.loansimulator.infrastructure.gateway.currency;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.CurrencyExchangeRateClient;
import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyExchangeRateDto;
import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyPriceDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateSchedulerTest {

  @Mock private CurrencyExchangeRateClient currencyExchangeRateClient;

  @Mock private ExchangeRateServiceImpl exchangeRateServiceImpl;

  @InjectMocks private CurrencyExchangeRateScheduler currencyExchangeRateScheduler;

  private CurrencyPriceDto usdPrice;
  private CurrencyPriceDto eurPrice;
  private CurrencyPriceDto gbpPrice;
  private CurrencyPriceDto cnyPrice;
  private CurrencyExchangeRateDto exchangeRateDto;

  @BeforeEach
  void setUp() {
    usdPrice = new CurrencyPriceDto(Currency.USD, new BigDecimal("5.00"), new BigDecimal("4.90"));
    eurPrice = new CurrencyPriceDto(Currency.EUR, new BigDecimal("5.50"), new BigDecimal("5.40"));
    gbpPrice = new CurrencyPriceDto(Currency.GBP, new BigDecimal("6.20"), new BigDecimal("6.10"));
    cnyPrice = new CurrencyPriceDto(Currency.CNY, new BigDecimal("0.70"), new BigDecimal("0.68"));

    exchangeRateDto = new CurrencyExchangeRateDto(usdPrice, eurPrice, gbpPrice, cnyPrice);
  }

  @Test
  @DisplayName("Should update exchange rates successfully when client returns valid data")
  void shouldUpdateExchangeRatesSuccessfullyWhenClientReturnsValidData() {
    when(currencyExchangeRateClient.getExchangeRate()).thenReturn(Mono.just(exchangeRateDto));

    currencyExchangeRateScheduler.getNewExchangeRate();

    verify(currencyExchangeRateClient, times(1)).getExchangeRate();
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.USD, usdPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.EUR, eurPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.GBP, gbpPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.CNY, cnyPrice.calculateAveragePrice());
  }

  @Test
  @DisplayName("Should handle empty exchange rate list when client returns an empty mono")
  void shouldHandleEmptyExchangeRateListWhenClientReturnsEmptyMono() {
    when(currencyExchangeRateClient.getExchangeRate()).thenReturn(Mono.empty());

    currencyExchangeRateScheduler.getNewExchangeRate();

    verify(currencyExchangeRateClient, times(1)).getExchangeRate();
    verifyNoInteractions(ignoreStubs(exchangeRateServiceImpl));
  }

  @Test
  @DisplayName("Should call getNewExchangeRate method on startup")
  void shouldCallGetNewExchangeRateMethodOnStartup() {
    when(currencyExchangeRateClient.getExchangeRate()).thenReturn(Mono.just(exchangeRateDto));

    currencyExchangeRateScheduler.runOnStartup();

    verify(currencyExchangeRateClient, times(1)).getExchangeRate();
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.USD, usdPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.EUR, eurPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.GBP, gbpPrice.calculateAveragePrice());
    verify(exchangeRateServiceImpl, times(1))
        .updateRate(Currency.CNY, cnyPrice.calculateAveragePrice());
  }
}

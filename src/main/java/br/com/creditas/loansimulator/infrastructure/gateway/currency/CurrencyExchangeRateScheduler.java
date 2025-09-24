package br.com.creditas.loansimulator.infrastructure.gateway.currency;

import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.CurrencyExchangeRateClient;
import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyExchangeRateDto;
import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyPriceDto;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyExchangeRateScheduler {

  private final CurrencyExchangeRateClient currencyExchangeRateClient;
  private final ExchangeRateServiceImpl exchangeRateServiceImpl;

  @PostConstruct
  public void runOnStartup() {
    this.getNewExchangeRate();
  }

  @Scheduled(cron = "${schedule.get-new-exchange-rate-cron-pattern}")
  public void getNewExchangeRate() {
    log.info("Updating exchange rates");

    List<CurrencyPriceDto> exchangeRateList =
        currencyExchangeRateClient
            .getExchangeRate()
            .map(CurrencyExchangeRateDto::toList)
            .blockOptional()
            .orElse(Collections.emptyList());

    exchangeRateList.forEach(
        exchangeRate ->
            exchangeRateServiceImpl.updateRate(
                exchangeRate.currency(), exchangeRate.calculateAveragePrice()));
  }
}

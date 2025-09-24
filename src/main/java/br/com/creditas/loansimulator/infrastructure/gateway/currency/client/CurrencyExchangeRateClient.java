package br.com.creditas.loansimulator.infrastructure.gateway.currency.client;

import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateClient {

  private final WebClient webClient;

  @Value("${api.exchange.url}")
  private String host;

  public Mono<CurrencyExchangeRateDto> getExchangeRate() {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .host(host)
                    .scheme("https")
                    .path("/last/USD-BRL,EUR-BRL,GBP-BRL,CNY-BRL")
                    .build())
        .retrieve()
        .bodyToMono(CurrencyExchangeRateDto.class);
  }
}

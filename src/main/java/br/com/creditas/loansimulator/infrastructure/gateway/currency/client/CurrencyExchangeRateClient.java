package br.com.creditas.loansimulator.infrastructure.gateway.currency.client;

import br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto.CurrencyExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "${api.exchange.url}", name = "exchange")
public interface CurrencyExchangeRateClient {

    @GetMapping("/USD-BRL,EUR-BRL,GBP-BRL,CNY-BRL")
    CurrencyExchangeRateDto getExchangeRate();

}

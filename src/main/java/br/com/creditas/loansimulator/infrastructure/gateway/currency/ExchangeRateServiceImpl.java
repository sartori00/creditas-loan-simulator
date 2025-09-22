package br.com.creditas.loansimulator.infrastructure.gateway.currency;

import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Map<Currency, BigDecimal> exchangeRates = new ConcurrentHashMap<>();

    public ExchangeRateServiceImpl() {
        exchangeRates.put(Currency.BRL, BigDecimal.ONE);
    }

    @Override
    public BigDecimal getRateToBRL(Currency currency) {
        return exchangeRates.get(currency);
    }

    public void updateRate(Currency currency, BigDecimal rate) {
        if (currency != null && rate != null) {
            exchangeRates.put(currency, rate);
        }
        log.info("{} updated to {}", currency, rate);
    }
}

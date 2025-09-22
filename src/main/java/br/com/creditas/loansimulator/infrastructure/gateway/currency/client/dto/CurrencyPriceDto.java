package br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto;

import br.com.creditas.loansimulator.domain.model.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CurrencyPriceDto(@JsonProperty("code") Currency currency,
                               BigDecimal high,
                               BigDecimal low) {


    public BigDecimal calculateAveragePrice(){
        var sum = this.low.add(this.high);
        return sum.divide(new BigDecimal("2"), 2, RoundingMode.UP);
    }
}

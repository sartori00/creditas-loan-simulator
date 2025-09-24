package br.com.creditas.loansimulator.infrastructure.gateway.currency.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CurrencyExchangeRateDto(@JsonProperty("USDBRL") CurrencyPriceDto americanDollar,
                                      @JsonProperty("EURBRL") CurrencyPriceDto euro,
                                      @JsonProperty("GBPBRL") CurrencyPriceDto poundsSterling,
                                      @JsonProperty("CNYBRL") CurrencyPriceDto chineseYuan) {

    public List<CurrencyPriceDto> toList(){
        return List.of(americanDollar, euro, poundsSterling, chineseYuan);

    }
}

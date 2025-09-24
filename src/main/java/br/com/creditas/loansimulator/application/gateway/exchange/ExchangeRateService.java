package br.com.creditas.loansimulator.application.gateway.exchange;

import br.com.creditas.loansimulator.domain.model.enums.Currency;
import java.math.BigDecimal;

public interface ExchangeRateService {
  BigDecimal getRateToBRL(Currency currency);
}

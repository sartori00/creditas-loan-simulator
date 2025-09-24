package br.com.creditas.loansimulator.infrastructure.config.bean.usecase;

import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.application.usecase.impl.LoanSimulatorUseCaseImpl;
import br.com.creditas.loansimulator.domain.service.FixedPaymentCalculator;
import br.com.creditas.loansimulator.domain.strategy.RangesStrategy;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanSimulatorUseCaseConfig {

  @Bean
  public LoanSimulatorUseCase loanSimulatorUseCase(
      ExchangeRateService exchangeRateService,
      List<RangesStrategy> rangesStrategies,
      FixedPaymentCalculator fixedPaymentCalculator) {
    return new LoanSimulatorUseCaseImpl(
        exchangeRateService, rangesStrategies, fixedPaymentCalculator);
  }
}

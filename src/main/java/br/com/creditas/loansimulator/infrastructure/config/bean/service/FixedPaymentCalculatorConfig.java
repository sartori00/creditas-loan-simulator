package br.com.creditas.loansimulator.infrastructure.config.bean.service;

import br.com.creditas.loansimulator.domain.service.FixedPaymentCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FixedPaymentCalculatorConfig {

  @Bean
  public FixedPaymentCalculator fixedPaymentCalculator() {
    return new FixedPaymentCalculator();
  }
}

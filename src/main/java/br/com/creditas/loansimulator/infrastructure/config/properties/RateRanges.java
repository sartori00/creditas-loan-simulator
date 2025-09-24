package br.com.creditas.loansimulator.infrastructure.config.properties;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rate.properties")
@Getter
@Setter
public class RateRanges {

  @Value("${rate.first-range}")
  private BigDecimal firstRateRange;

  @Value("${rate.second-range}")
  private BigDecimal secondRateRange;

  @Value("${rate.third-range}")
  private BigDecimal thirdRateRange;

  @Value("${rate.fourth-range}")
  private BigDecimal fourthRateRange;
}

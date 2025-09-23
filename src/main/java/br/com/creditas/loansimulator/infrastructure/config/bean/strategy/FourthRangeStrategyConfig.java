package br.com.creditas.loansimulator.infrastructure.config.bean.strategy;

import br.com.creditas.loansimulator.domain.strategy.impl.FourthRangeStrategy;
import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FourthRangeStrategyConfig {

    @Bean
    public FourthRangeStrategy fourthRangeStrategy(RateRanges rateRanges){
        return new FourthRangeStrategy(rateRanges);
    }
}

package br.com.creditas.loansimulator.infrastructure.config.bean.strategy;

import br.com.creditas.loansimulator.domain.strategy.impl.FirstRangeStrategy;
import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirstRangeStrategyConfig {

    @Bean
    public FirstRangeStrategy firstRangeStrategy(RateRanges rateRanges){
        return new FirstRangeStrategy(rateRanges);
    }
}

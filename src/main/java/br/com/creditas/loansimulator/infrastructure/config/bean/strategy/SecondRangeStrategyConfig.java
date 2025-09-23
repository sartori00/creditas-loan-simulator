package br.com.creditas.loansimulator.infrastructure.config.bean.strategy;

import br.com.creditas.loansimulator.domain.strategy.impl.SecondRangeStrategy;
import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecondRangeStrategyConfig {

    @Bean
    public SecondRangeStrategy secondRangeStrategy(RateRanges rateRanges){
        return new SecondRangeStrategy(rateRanges);
    }
}

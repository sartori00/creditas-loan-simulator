package br.com.creditas.loansimulator.infrastructure.config.bean.strategy;

import br.com.creditas.loansimulator.domain.strategy.impl.ThirdRangeStrategy;
import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThirdRangeStrategyConfig {

    @Bean
    public ThirdRangeStrategy thirdRangeStrategy(RateRanges rateRanges){
        return new ThirdRangeStrategy(rateRanges);
    }
}

package br.com.creditas.loansimulator.domain.strategy.impl;

import br.com.creditas.loansimulator.domain.strategy.RangesStrategy;
import br.com.creditas.loansimulator.infrastructure.config.properties.RateRanges;

import java.math.BigDecimal;
import java.math.MathContext;

public class ThirdRangeStrategy implements RangesStrategy {

    private static final int RANGE_BEGINNING = 41;
    private static final int RANGE_FINAL = 60;
    private static final BigDecimal ONE_YEAR = new BigDecimal(12);
    private final BigDecimal rateRange;


    public ThirdRangeStrategy(RateRanges rateRanges) {
        this.rateRange = rateRanges.getThirdRateRange();
    }

    @Override
    public boolean isThisRange(int age){
        return age >= RANGE_BEGINNING && age <= RANGE_FINAL;
    }

    @Override
    public BigDecimal monthlyRateCalculation(){
        return rateRange.divide(ONE_YEAR, MathContext.DECIMAL128);
    }
}

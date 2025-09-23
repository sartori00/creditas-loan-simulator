package br.com.creditas.loansimulator.domain.strategy;

import java.math.BigDecimal;

public interface RangesStrategy {
    boolean isThisRange(int age);

    BigDecimal monthlyRateCalculation();
}

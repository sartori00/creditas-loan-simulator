package br.com.creditas.loansimulator.application.usecase.impl;

import br.com.creditas.loansimulator.application.exceptions.UnsupportedAgeException;
import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.domain.strategy.RangesStrategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class LoanSimulatorUseCaseImpl implements LoanSimulatorUseCase {

    private final ExchangeRateService exchangeRateService;
    private final List<RangesStrategy> rangesStrategies;

    public LoanSimulatorUseCaseImpl(ExchangeRateService exchangeRateService, List<RangesStrategy> rangesStrategies) {
        this.exchangeRateService = exchangeRateService;
        this.rangesStrategies = rangesStrategies;
    }

    @Override
    public LoanSimulation execute(LoanSimulation loanSimulation) {
        var amountBRL = this.convertToBRL(loanSimulation.getCurrency(), loanSimulation.getLoanAmount());

        var monthlyRate = this.getMonthlyRate(loanSimulation.getPerson().getBirthDay());

        // calcular

        // mandar a persistencia e o envio do email para o async ou fazer via observable
        return null;
    }

    private BigDecimal convertToBRL(Currency currency, BigDecimal loanAmount){
        var exchangeRate = exchangeRateService.getRateToBRL(currency);

        return loanAmount.multiply(exchangeRate);
    }

    private int getAge(LocalDate birthday){
        var currentDate = LocalDate.now();
        return Period.between(birthday, currentDate)
                .getYears();
    }

    private BigDecimal getMonthlyRate(LocalDate birthDay){
        var age = this.getAge(birthDay);

        return rangesStrategies.stream()
                .filter(rangesStrategy -> rangesStrategy.isThisRange(age))
                .findFirst()
                .orElseThrow(UnsupportedAgeException::new)
                .monthlyRateCalculation();
    }
}

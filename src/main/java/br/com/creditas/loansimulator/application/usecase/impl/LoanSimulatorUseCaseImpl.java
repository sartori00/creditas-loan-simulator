package br.com.creditas.loansimulator.application.usecase.impl;

import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LoanSimulatorUseCaseImpl implements LoanSimulatorUseCase {

    private final ExchangeRateService exchangeRateService;

    public LoanSimulatorUseCaseImpl(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    public LoanSimulation execute(LoanSimulation loanSimulation) {
        var amountBRL = this.convertToBRL(loanSimulation.getCurrency(), loanSimulation.getLoanAmount());

        // obter idade

        // obter a regra de calculo com base no strategy

        // calcular

        // mandar a persistencia e o envio do email para o async ou fazer via observable
        return null;
    }

    private BigDecimal convertToBRL(Currency currency, BigDecimal loanAmount){
        var exchangeRate = exchangeRateService.getRateToBRL(currency);

        return loanAmount.multiply(exchangeRate);
    }

}

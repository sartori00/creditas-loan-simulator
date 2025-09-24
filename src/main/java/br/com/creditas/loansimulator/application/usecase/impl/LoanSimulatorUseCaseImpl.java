package br.com.creditas.loansimulator.application.usecase.impl;

import br.com.creditas.loansimulator.application.exceptions.BusinessException;
import br.com.creditas.loansimulator.application.exceptions.UnsupportedAgeException;
import br.com.creditas.loansimulator.application.gateway.exchange.ExchangeRateService;
import br.com.creditas.loansimulator.application.usecase.LoanSimulatorUseCase;
import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.service.FixedPaymentCalculator;
import br.com.creditas.loansimulator.domain.strategy.RangesStrategy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class LoanSimulatorUseCaseImpl implements LoanSimulatorUseCase {

  private final ExchangeRateService exchangeRateService;
  private final List<RangesStrategy> rangesStrategies;
  private final FixedPaymentCalculator fixedPaymentCalculator;

  public LoanSimulatorUseCaseImpl(
      ExchangeRateService exchangeRateService,
      List<RangesStrategy> rangesStrategies,
      FixedPaymentCalculator fixedPaymentCalculator) {
    this.exchangeRateService = exchangeRateService;
    this.rangesStrategies = rangesStrategies;
    this.fixedPaymentCalculator = fixedPaymentCalculator;
  }

  @Override
  public LoanSimulation execute(LoanSimulation loanSimulation) {
    try {
      this.convertToBRL(loanSimulation);

      var monthlyRate = this.getMonthlyRate(loanSimulation.getPerson().getBirthDay());

      var installmentAmount =
          fixedPaymentCalculator.calculate(
              monthlyRate, loanSimulation.getLoanAmountBRL(), loanSimulation.getQtInstallments());

      loanSimulation.setInstallmentAmount(installmentAmount);

      return loanSimulation;
    } catch (IllegalArgumentException | UnsupportedAgeException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  private void convertToBRL(LoanSimulation loanSimulation) {
    var exchangeRate = exchangeRateService.getRateToBRL(loanSimulation.getCurrency());

    var amountBRL = loanSimulation.getLoanAmount().multiply(exchangeRate);

    loanSimulation.setLoanAmountBRL(amountBRL);
  }

  private BigDecimal getMonthlyRate(LocalDate birthDay) {
    var age = this.getAge(birthDay);

    return rangesStrategies.stream()
        .filter(rangeStrategy -> rangeStrategy.isThisRange(age))
        .findFirst()
        .orElseThrow(UnsupportedAgeException::new)
        .monthlyRateCalculation();
  }

  private int getAge(LocalDate birthday) {
    var currentDate = LocalDate.now();
    return Period.between(birthday, currentDate).getYears();
  }
}

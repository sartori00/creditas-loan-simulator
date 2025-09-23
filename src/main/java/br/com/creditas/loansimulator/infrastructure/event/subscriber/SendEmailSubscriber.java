package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
import br.com.creditas.loansimulator.domain.model.enums.Currency;
import br.com.creditas.loansimulator.infrastructure.event.NewLoanCalculatedObservable;
import br.com.creditas.loansimulator.infrastructure.gateway.email.MailServiceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendEmailSubscriber implements ApplicationListener<NewLoanCalculatedObservable> {

    private final MailServiceAdapter mailServiceAdapter;

    @Override
    @Async("eventSubscribersTaskExecutor")
    public void onApplicationEvent(NewLoanCalculatedObservable event) {
        var loanSimulation = event.getLoanSimulation();
        log.info("Sending async email for {}", loanSimulation.getPerson().getEmail());

        var content = this.buildEmailContent(loanSimulation);

        mailServiceAdapter.sendEmail(loanSimulation, content);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private String buildEmailContent(LoanSimulation loanSimulation){
        return String.format(
                "<html><body>" +
                        "<h2>Olá %s,</h2>" +
                        "<p>Sua simulação de empréstimo foi calculada com sucesso!</p>" +
                        "<p>Detalhes da simulação:</p>" +
                        "<ul>" +
                        "<li>Valor solicitado para Empréstimo: %s %s</li>" +
                        "<li>Número de Parcelas: %d</li>" +
                        "<li>Valor das Parcelas mensais: %s %s</li>" +
                        "<li>Valor total a ser pago: %s %s</li>" +
                        "<li>Valor total de juros a ser pago: %s %s</li>" +
                        "</ul>" +
                        "<p>Obrigado por usar nosso simulador!</p>" +
                        "</body></html>",
                loanSimulation.getPerson().getEmail(),
                loanSimulation.getCurrency().getSymbol(), loanSimulation.getLoanAmount(),
                loanSimulation.getQtInstallments(),
                Currency.BRL.getSymbol(), loanSimulation.getInstallmentAmount(),
                Currency.BRL.getSymbol(), loanSimulation.getTotalAmountToPay(),
                Currency.BRL.getSymbol(), loanSimulation.getTotalInterest()
        );
    }
}

package br.com.creditas.loansimulator.infrastructure.event.subscriber;

import br.com.creditas.loansimulator.domain.model.LoanSimulation;
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
                        "<li>Valor do Empréstimo: %s %s</li>" +
                        "<li>Número de Parcelas: %d</li>" +
                        "<li>Valor da Parcela: %s %s</li>" +
                        "<li>Total a Pagar: %s %s</li>" +
                        "</ul>" +
                        "<p>Obrigado por usar nosso simulador!</p>" +
                        "</body></html>",
                loanSimulation.getPerson().getEmail(),
                loanSimulation.getCurrency(), loanSimulation.getLoanAmount(),
                loanSimulation.getQtInstallments(),
                loanSimulation.getCurrency(), loanSimulation.getInstallmentAmount(),
                loanSimulation.getCurrency(), loanSimulation.getTotalAmountToPay()
        );
    }
}

package br.com.creditas.loansimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableJpaAuditing
public class LoanSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanSimulatorApplication.class, args);
	}

}

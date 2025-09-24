package br.com.creditas.loansimulator.infrastructure.config.async;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "eventSubscribersTaskExecutor")
  public Executor eventSubscribersTaskExecutor() {
    return new VirtualThreadTaskExecutor("subscriberEvent-virtual-thread-");
  }
}

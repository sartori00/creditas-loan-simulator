package br.com.creditas.loansimulator.infrastructure.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "eventSubscribersTaskExecutor")
    public Executor eventSubscribersTaskExecutor() {
        return new VirtualThreadTaskExecutor("subscriberEvent-virtual-thread-");
    }

    @Bean(name = "batchModeTaskExecutor")
    public Executor batchModeTaskExecutor() {
        return new VirtualThreadTaskExecutor("batchEvent-virtual-thread-");
    }
}

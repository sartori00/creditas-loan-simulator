package br.com.creditas.loansimulator.infrastructure.config.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;
import java.time.Duration;

@Configuration
public class AwsSqsConfigLocal {

    @Value("${aws.url:default}")
    private String awsUrl;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(awsUrl))
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .httpClientBuilder(NettyNioAsyncHttpClient.builder()
                        .maxConcurrency(200)
                        .connectionAcquisitionTimeout(Duration.ofSeconds(30))
                        .connectionTimeout(Duration.ofSeconds(10))
                        .writeTimeout(Duration.ofSeconds(40))
                        .readTimeout(Duration.ofSeconds(40)))
                .build();
    }
}
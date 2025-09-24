package br.com.creditas.loansimulator.infrastructure.config.queue;

import java.net.URI;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsSqsConfig {

  @Value("${aws.url:default}")
  private String awsUrl;

  @Value("${aws.access-key-id}")
  private String awsAccessKeyId;

  @Value("${aws.secret-access-key}")
  private String awsSecretAccessKey;

  @Bean
  public SqsAsyncClient sqsAsyncClient() {
    return SqsAsyncClient.builder()
        .endpointOverride(URI.create(awsUrl))
        .region(Region.US_EAST_1)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey)))
        .httpClientBuilder(
            NettyNioAsyncHttpClient.builder()
                .maxConcurrency(350)
                .connectionAcquisitionTimeout(Duration.ofSeconds(60))
                .connectionTimeout(Duration.ofSeconds(10))
                .writeTimeout(Duration.ofSeconds(40))
                .readTimeout(Duration.ofSeconds(40)))
        .build();
  }
}

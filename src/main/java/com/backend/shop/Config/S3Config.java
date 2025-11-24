package com.backend.shop.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    // AWS region from application.properties
    @Value("${cloud.aws.region}")
    private String region;

    /**
     * Create and expose S3Client as a Spring Bean.
     * AWS credentials are loaded from environment variables:
     * - AWS_ACCESS_KEY_ID
     * - AWS_SECRET_ACCESS_KEY
     *
     * This is the safest and most recommended approach for EC2 and local development.
     */
    @Bean("s3Client")
    @Profile("local")
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    @Bean("s3Client")
    @Profile("dev")
    public S3Client s3ClientDev() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}

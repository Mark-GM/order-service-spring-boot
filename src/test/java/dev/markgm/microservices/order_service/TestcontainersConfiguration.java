package dev.markgm.microservices.order_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        final DockerImageName postgresImageName = DockerImageName
                .parse("postgres:17.6@sha256:ae3afa4af0906431de8856bf80a8bcf8a9ea6b3609f9e025f927b949ac93467d")
                .asCompatibleSubstituteFor("postgres");
        return new PostgreSQLContainer<>(postgresImageName);
    }

}

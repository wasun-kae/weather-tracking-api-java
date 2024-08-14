package kae.wasun.weather.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainersConfig {

    @Bean
    public LocalStackContainer localStackContainer() {
        var imageName = DockerImageName.parse("localstack/localstack:3.6");

        try (var localStackContainer = new LocalStackContainer(imageName)) {
            return localStackContainer.withServices(LocalStackContainer.Service.DYNAMODB);
        }
    }
}

package ro.ubb.catalog.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by radu.
 */
@Configuration
public class ClientConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

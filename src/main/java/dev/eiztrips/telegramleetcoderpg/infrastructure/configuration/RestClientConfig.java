package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

	@Bean
	public RestClient.Builder restClientBuilder() {
		var requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout((int) Duration.ofSeconds(3).toMillis());
		requestFactory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());

		return RestClient.builder().requestFactory(requestFactory);
	}
}

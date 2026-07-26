package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.eiztrips;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.eiztrips.EiztripsClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.BossCredentialsData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class EiztripsApiAdapter implements EiztripsClientPort {

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	private final Random RANDOM;
	private final String REQUEST_URL;
	private final Integer MIN_HP;
	private final Integer MAX_HP;
	private final String FALLBACK_NAME;
	private final List<String> ADJECTIVES;

	public EiztripsApiAdapter(HttpClient httpClient, ObjectMapper objectMapper, @Value("${boss.hp.min:100}") int minHp,
			@Value("${boss.hp.max:500}") int maxHp, @Value("${boss.fallback.name}") String fallbackName,
			@Value("${boss.random.name.api}") String url, @Value("${boss.adjectives.file}") Resource adjectivesResource)
			throws IOException {
		this.httpClient = httpClient;
		this.objectMapper = objectMapper;
		this.MIN_HP = minHp;
		this.MAX_HP = maxHp;
		this.REQUEST_URL = url;
		this.FALLBACK_NAME = fallbackName;
		this.ADJECTIVES = loadAdjectives(adjectivesResource);
		this.RANDOM = ThreadLocalRandom.current();
	}

	private List<String> loadAdjectives(Resource resource) throws IOException {
		try (var is = resource.getInputStream();
				var reader = new java.io.BufferedReader(
						new java.io.InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {

			var res = reader.lines().filter(s -> !s.isBlank()).map(String::trim).toList();

			if (res.isEmpty()) {
				log.warn("Не удалось загрузить прилагательные");
				return List.of("Храбрый", "Глупый", "Пивной");
			}

			log.info("Загружено {} прилагательных", res.size());
			return res;
		}
	}

	@Override
	public BossCredentialsData fetchRandomBossCredentials() {

		var request = HttpRequest.newBuilder().uri(URI.create(REQUEST_URL)).header("Accept", "application/json")
				.build();

		var name = FALLBACK_NAME;

		try {
			log.info("Послан запрос на %s".formatted(REQUEST_URL));
			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				var json = objectMapper.readTree(response.body());

				if (json.has("word"))
					name = json.get("word").asString();
			}
		} catch (IOException _) {
			log.error("IOException выброшен во время выполнения fetchRandomBossCreditals");
		} catch (InterruptedException _) {
			Thread.currentThread().interrupt();
		}

		return BossCredentialsData.builder().adjective(getAdjective()).name(name).hp(RANDOM.nextInt(MIN_HP, MAX_HP + 1))
				.build();
	}

	private String getAdjective() {
		return ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
	}
}

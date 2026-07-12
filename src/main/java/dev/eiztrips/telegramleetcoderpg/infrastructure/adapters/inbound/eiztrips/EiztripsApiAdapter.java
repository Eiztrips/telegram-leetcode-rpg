package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.eiztrips;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.eiztrips.EiztripsClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.BossCredentialsData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class EiztripsApiAdapter implements EiztripsClientPort {

	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	private final String REQUEST_URL = "https://eiztrips.dev/api/fwd/funny-word";
	private static final Random RANDOM = ThreadLocalRandom.current();
	private static final String[] ADJECTIVES = {"Великий", "Ультразвуковой", "Свирепый", "Чешуйчатый", "Пухленький",
			"Загадочный", "Подпивасный", "Древний", "Межгалактический", "Сутулый", "Яростный", "Криворукий", "Грозный",
			"Злобный", "Коварный"};

	@Override
	public BossCredentialsData fetchRandomBossCredentials() {

		var request = HttpRequest.newBuilder().uri(URI.create(REQUEST_URL)).header("Accept", "application/json")
				.build();

		var name = "Разрушитель api Eiztrips'a";

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

		return BossCredentialsData.builder().adjective(getAdjective()).name(name).hp(RANDOM.nextInt(100, 501)).build();
	}

	private String getAdjective() {
		return ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
	}
}

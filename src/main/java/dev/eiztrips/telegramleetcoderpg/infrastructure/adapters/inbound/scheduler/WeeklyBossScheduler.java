package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.scheduler;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.RespawnWeeklyBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyBossScheduler {

	private final RespawnWeeklyBossUseCase respawnWeeklyBossUseCase;
	private final BossRepositoryPort bossRepositoryPort;
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;

	@Scheduled(cron = "0 0 0 * * SUN")
	@Transactional
	public void cronRespawnBoss() {
		log.info("Еженедельный респавн боссов");
		triggerRespawn();
	}

	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void onStartupRespawnBosses() {
		log.info("Проверка необходимости респавна боссов:");
		LocalDate currDate = bossRepositoryPort.getLastRespawnDate();
		triggerRespawn();
		LocalDate newDate = bossRepositoryPort.getLastRespawnDate();
		if (!currDate.isEqual(newDate))
			log.info("Недельный босс обновлен");
	}

	private void triggerRespawn() {
		var bossCredentials = fetchRandomBossCredentials();
		respawnWeeklyBossUseCase.respawnWeeklyBoss(bossCredentials.get("adjective") + " " + bossCredentials.get("name"),
				(int) bossCredentials.get("hp"), null);
		log.info("Последняя дата обновления: {}", bossRepositoryPort.getLastRespawnDate().toString());
		log.info("Текущий босс недели: {}", bossRepositoryPort.getCurrentWeeklyBoss());
	}

	private Map<String, Object> fetchRandomBossCredentials() {
		var map = new HashMap<String, Object>();

		var request = HttpRequest.newBuilder().uri(URI.create("https://eiztrips.dev/api/fwd/funny-word"))
				.header("Accept", "application/json").build();

		var name = "Разрушитель api Eiztrips'a";

		try {
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

		map.put("adjective", getAdjective());
		map.put("name", name);
		map.put("hp", ThreadLocalRandom.current().nextInt(100, 501));

		return map;
	}

	private String getAdjective() {
		return ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
	}

	private static final Random RANDOM = new Random();
	private static final String[] ADJECTIVES = {"Великий", "Ультразвуковой", "Свирепый", "Чешуйчатый", "Пухленький",
			"Загадочный", "Подпивасный", "Древний", "Межгалактический", "Сутулый", "Яростный", "Криворукий", "Грозный",
			"Злобный", "Коварный"};
}

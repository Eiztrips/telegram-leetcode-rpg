package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.scheduler;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.guild.RespawnWeeklyBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.eiztrips.EiztripsClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss.BossRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyBossScheduler {

	private final RespawnWeeklyBossUseCase respawnWeeklyBossUseCase;
	private final BossRepositoryPort bossRepositoryPort;
	private final EiztripsClientPort eiztripsClientPort;

	@Scheduled(cron = "@weekly")
	public void cronRespawnBoss() {
		log.info("Еженедельный респавн боссов");
		triggerRespawn();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartupRespawnBosses() {
		log.info("Проверка необходимости респавна боссов:");
		LocalDate currDate = bossRepositoryPort.getLastRespawnDate();
		triggerRespawn();
		LocalDate newDate = bossRepositoryPort.getLastRespawnDate();
		if (!currDate.isEqual(newDate))
			log.info("Недельный босс обновлен");
	}

	private void triggerRespawn() {
		var bossCredentials = eiztripsClientPort.fetchRandomBossCredentials();
		respawnWeeklyBossUseCase.respawnWeeklyBoss(bossCredentials.adjective() + " " + bossCredentials.name(),
				bossCredentials.hp(), null);
		log.info("Последняя дата обновления: {}", bossRepositoryPort.getLastRespawnDate().toString());
		log.info("Текущий босс недели: {}", bossRepositoryPort.getCurrentWeeklyBoss());
	}
}

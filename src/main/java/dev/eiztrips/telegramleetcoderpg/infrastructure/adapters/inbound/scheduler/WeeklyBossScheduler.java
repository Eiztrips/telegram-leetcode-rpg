package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.scheduler;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.RespawnWeeklyBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
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

	@Scheduled(cron = "0 0 0 * * SUN")
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
		respawnWeeklyBossUseCase.respawnWeeklyBoss("TEST", 100, null);
		log.info("Последняя дата обновления: {}", bossRepositoryPort.getLastRespawnDate().toString());
		log.info("Текущий босс недели: {}", bossRepositoryPort.getCurrentWeeklyBoss());
	}
}

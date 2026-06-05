package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.WeeklyBossExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.core.ports.inbound.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.core.ports.outbound.BossRepositoryPort;

/**
 * Сервис боя с боссом.
 */
public final class BossFightService implements AttackBossUseCase {

	private final BossRepositoryPort bossRepositoryPort;

	/**
	 * Создает сервис боя с боссом.
	 *
	 * @param bossRepositoryPort
	 *            порт репозитория боссов
	 */
	public BossFightService(BossRepositoryPort bossRepositoryPort) {
		this.bossRepositoryPort = bossRepositoryPort;
	}

	@Override
	public WeeklyBoss attackBoss(Long bossId, int damage) {
		WeeklyBoss boss = bossRepositoryPort.getById(bossId)
				.orElseThrow(() -> new WeeklyBossExceptions.WeeklyBossNotFoundException(bossId));

		boss = boss.takeDamage(damage);

		bossRepositoryPort.save(boss);

		return boss;
	}
}

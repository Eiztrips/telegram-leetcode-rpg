package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;

import java.util.List;

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
	public WeeklyBoss attackBoss(Long bossId, List<SubmissionData> submissionDataList) {
		WeeklyBoss boss = bossRepositoryPort.getById(bossId)
				.orElseThrow(() -> new WeeklyBossExceptions.WeeklyBossNotFoundException(bossId));

		var damage = 0;

		for (SubmissionData sd : submissionDataList)
			damage += Difficulty.valueOf(sd.taskDifficulty()).getReward();

		boss = boss.takeDamage(damage);

		bossRepositoryPort.save(boss);

		return boss;
	}
}

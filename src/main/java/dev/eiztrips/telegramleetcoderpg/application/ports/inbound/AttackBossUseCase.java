package dev.eiztrips.telegramleetcoderpg.application.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions.*;

import java.util.List;

/**
 * Сценарий использования: атака босса.
 */
public interface AttackBossUseCase {
	/**
	 * Атаковать еженедельного босса.
	 * 
	 * @param bossId
	 *            telegramId босса
	 * @param submissionDataList
	 *            сабмишены пользователя
	 * @return еженедельный босс с обновленным здоровьем
	 *
	 * @throws WeeklyBossNotFoundException
	 *             босс с данным telegramId не найден
	 * @throws WeeklyBossAlreadyDefeated
	 *             босс уже был побежден
	 */
	WeeklyBoss attackBoss(Long bossId, List<SubmissionData> submissionDataList);
}

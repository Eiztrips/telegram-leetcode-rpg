package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.boss;

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
	 * @param submissionDataList
	 *            сабмишены пользователя
	 * @param userId
	 *            id пользователя
	 * @return еженедельный босс с обновленным здоровьем
	 *
	 * @throws WeeklyBossNotFoundException
	 *             босс с данным telegramId не найден
	 * @throws WeeklyBossAlreadyDefeated
	 *             босс уже был побежден
	 */
	WeeklyBoss attackBoss(List<SubmissionData> submissionDataList, Long userId);
}

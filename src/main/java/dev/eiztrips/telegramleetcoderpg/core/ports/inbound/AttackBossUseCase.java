package dev.eiztrips.telegramleetcoderpg.core.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions.UserNotFoundException;
import dev.eiztrips.telegramleetcoderpg.core.domain.exception.WeeklyBossExceptions.*;

/**
 * Сценарий использования: атака босса.
 */
public interface AttackBossUseCase {
	/**
	 * Атаковать еженедельного босса.
	 * 
	 * @param bossId
	 *            telegramId босса
	 * @param damage
	 *            нанесенный урон
	 * @return еженедельный босс с обновленным здоровьем
	 *
	 * @throws WeeklyBossNotFoundException
	 *             босс с данным telegramId не найден
	 * @throws UserNotFoundException
	 *             пользователь с данным telegramId не найден
	 */
	WeeklyBoss attackBoss(Long bossId, int damage);
}

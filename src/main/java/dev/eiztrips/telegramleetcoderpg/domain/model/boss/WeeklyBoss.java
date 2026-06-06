package dev.eiztrips.telegramleetcoderpg.domain.model.boss;

import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;

/**
 * Модель еженедельного босса.
 */
public record WeeklyBoss(Long id, String name, int maxHp, int currentHp) {
	/**
	 * Получить урон.
	 *
	 * @param damage
	 *            нанесенный урон
	 * @return обновленный босс с вычтенным здоровьем
	 */
	public WeeklyBoss takeDamage(int damage) {
		if (damage < 0)
			throw new WeeklyBossExceptions.InvalidDamageException();
		if (currentHp == 0)
			throw new WeeklyBossExceptions.WeeklyBossAlreadyDefeated(id);
		int newHp = Math.max(currentHp - damage, 0);
		return new WeeklyBoss(id, name, maxHp, newHp);
	}
}

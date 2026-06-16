package dev.eiztrips.telegramleetcoderpg.domain.model.boss;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;
import lombok.Builder;

/**
 * Модель еженедельного босса.
 */
public record WeeklyBoss(Long id, String name, int maxHp, int currentHp, Long version, Long guildId) {

	@Builder
	public WeeklyBoss {
		if (name == null || name.isBlank())
			throw new GlobalExceptions.ArgumentEmptyException("name");
		if (maxHp < 0 || currentHp < 0)
			throw new GlobalExceptions.ArgumentInvalidException("Здоровье не может быть отрицательным.");
		if (currentHp > maxHp)
			throw new GlobalExceptions.ArgumentInvalidException("Текущее здоровье не может превышать максимальное.");
	}

	/**
	 * Получить урон.
	 *
	 * @param damage
	 *            нанесенный урон
	 * @return обновленный босс с вычтенным здоровьем
	 */
	public WeeklyBoss takeDamage(int damage) {
		if (damage < 0)
			throw new GlobalExceptions.ArgumentInvalidException("Урон не может быть отрицательным.");
		if (currentHp == 0)
			throw new WeeklyBossExceptions.WeeklyBossAlreadyDefeated(id);
		int newHp = Math.max(currentHp - damage, 0);
		return new WeeklyBoss(id, name, maxHp, newHp, version, guildId);
	}
}

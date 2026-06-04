package dev.eiztrips.telegramleetcoderpg.core.ports.outbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.model.boss.WeeklyBoss;

import java.util.Optional;

/**
 * Порт репозитория боссов.
 */
public interface BossRepositoryPort {
	/**
	 * Сохранить босса.
	 *
	 * @param boss
	 *            босс
	 */
	void save(WeeklyBoss boss);

	/**
	 * Получить босса по telegramId.
	 *
	 * @param id
	 *            идентификатор босса
	 * @return босс, если существует
	 */
	Optional<WeeklyBoss> getById(Long id);
}

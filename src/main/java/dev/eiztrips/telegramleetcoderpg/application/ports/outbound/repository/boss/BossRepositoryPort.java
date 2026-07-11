package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;

import java.time.LocalDate;
import java.util.List;
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
	WeeklyBoss save(WeeklyBoss boss);

	/**
	 * Сохранить несколько боссов.
	 *
	 * @param bosses
	 *            боссы
	 * @return боссы
	 */
	List<WeeklyBoss> saveAll(List<WeeklyBoss> bosses);

	/**
	 * Получить босса по telegramId.
	 *
	 * @param id
	 *            идентификатор босса
	 * @return босс, если существует
	 */
	Optional<WeeklyBoss> getById(Long id);

	/**
	 * Получить момент последнего общего респавна.
	 *
	 * @return дата респавна
	 */
	LocalDate getLastRespawnDate();

	/**
	 * Обновить дату респавна.
	 *
	 * @param date
	 *            дата респавна
	 */
	void saveLastRespawnDate(LocalDate date);

	/**
	 * Обновить босса текущей недели.
	 *
	 * @return босс, если есть
	 */
	Optional<WeeklyBoss> getCurrentWeeklyBoss();

	/**
	 * Получить босса текущей недели.
	 */
	void saveCurrentWeeklyBoss(WeeklyBoss boss);
}

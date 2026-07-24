package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.guild;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;

import java.util.List;
import java.util.Optional;

/**
 * Порт репозитория гильдий.
 */
public interface GuildRepositoryPort {
	/**
	 * Сохранить гильдию
	 *
	 * @param guild
	 *            доменная модель
	 *
	 * @return гильдия
	 */
	Guild save(Guild guild);

	/**
	 * Сохранить несколько гильдий разом.
	 *
	 * @param guilds
	 *            доменная модель гильдии
	 * @return список гильдий
	 */
	List<Guild> saveAll(List<Guild> guilds);

	/**
	 * Удалить гильдию
	 *
	 * @param guildId
	 *            id гильдии
	 */
	void deleteById(Long guildId);

	/**
	 * Получить гильдию по guildId
	 *
	 * @param guildId
	 *            id гильдии
	 *
	 * @return гильдия, если существует
	 */
	Optional<Guild> getGuildById(Long guildId);

	/**
	 * Получить все гильдии
	 *
	 * @return список гильдий
	 */
	List<Guild> getAllGuilds();

	/**
	 * Получить текущего босса гильдии
	 *
	 * @return босс, если существует
	 */
	Optional<WeeklyBoss> getCurrentWeeklyBoss(Long guildId);
}

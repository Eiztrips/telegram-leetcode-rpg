package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild;

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
	 *            доменная модель гильдии
	 */
	void save(Guild guild);

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
}

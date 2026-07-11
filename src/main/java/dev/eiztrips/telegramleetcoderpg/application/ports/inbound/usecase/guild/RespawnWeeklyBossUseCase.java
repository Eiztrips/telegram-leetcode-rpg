package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions.GuildNotFoundException;

/**
 * Сценарий обновления еженедельного босса гильдии.
 */
public interface RespawnWeeklyBossUseCase {
	/**
	 * обновить еженедельного босса
	 *
	 * @param name
	 *            имя босса
	 * @param hp
	 *            здоровье босса
	 * @param guildId
	 *            id гильдии
	 *
	 * @throws GuildNotFoundException
	 *             гильдия не существует
	 */
	void respawnWeeklyBoss(String name, int hp, Long guildId);
}

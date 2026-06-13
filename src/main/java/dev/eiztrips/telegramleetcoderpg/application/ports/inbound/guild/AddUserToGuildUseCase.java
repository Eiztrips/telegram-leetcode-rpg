package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;

/**
 * Сценарий добавления пользователя в гильдию.
 */
public interface AddUserToGuildUseCase {
	/**
	 * Добавить пользвателя в гильдию.
	 *
	 * @param userId
	 *            id пользователя
	 * @param guildId
	 *            id гильдии
	 * @throws GuildNotFoundException
	 *             гильдия не найдена
	 * @throws UserNotFoundException
	 *             пользователь не найден
	 * @throws UserAlreadyExistsInGuild
	 *             пользователь уже находится в гильдии
	 *
	 */
	void addUserToGuild(Long userId, Long guildId);
}

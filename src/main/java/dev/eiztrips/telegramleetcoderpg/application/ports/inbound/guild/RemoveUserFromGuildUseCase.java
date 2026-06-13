package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;

/**
 * Сценарий удаления пользователя из гильдии.
 */
public interface RemoveUserFromGuildUseCase {
	/**
	 * удалить пользователя из гильдии.
	 *
	 * @param userId
	 *            id пользователя
	 * @param guildId
	 *            id гильдии
	 * @throws GuildNotFoundException
	 *             гильдия не найдена
	 * @throws UserNotFoundException
	 *             пользователь не найден
	 * @throws UserNotFoundInGuild
	 *             пользователь не находится в гильдии
	 */
	void removeUserFromGuild(Long userId, Long guildId);
}

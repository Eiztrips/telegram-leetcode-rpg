package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.guild;

import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.exception.ClientExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions.*;

/**
 * Сценарий создания гильдии.
 */
public interface CreateGuildUseCase {
	/**
	 * Создать гильдию.
	 *
	 * @param id
	 *            id гильдии
	 * @return созданная гильдия
	 * @throws ChatNotFoundException
	 *             чат не найден
	 * @throws GuildAlreadyExists
	 *             гильдия уже существует
	 */
	Guild create(Long id);
}

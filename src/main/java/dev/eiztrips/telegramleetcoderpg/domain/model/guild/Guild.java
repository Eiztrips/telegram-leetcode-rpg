package dev.eiztrips.telegramleetcoderpg.domain.model.guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import lombok.Builder;

/**
 * Доменная модель гильдии.
 *
 * @param version
 *            версия (race condition)
 * @param id
 *            уникальный индетификатор гильдии
 * @param currentBossId
 *            id текущего недельного босса
 */
public record Guild(Long version, Long id, Long currentBossId) {
	@Builder
	public Guild {
		if (id == null)
			throw new GlobalExceptions.ArgumentEmptyException("id");
	}
}

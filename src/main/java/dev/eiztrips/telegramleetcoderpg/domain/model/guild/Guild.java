package dev.eiztrips.telegramleetcoderpg.domain.model.guild;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import lombok.Builder;

public record Guild(Long version, Long id, Long currentBossId) {
	@Builder
	public Guild {
		if (id == null)
			throw new GlobalExceptions.ArgumentEmptyException("id");
	}
}

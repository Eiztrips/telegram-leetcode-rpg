package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import lombok.Builder;

/**
 * Информация об активности пользователя.
 *
 * @param user
 *            пользователь
 * @param isInactive
 *            активен ли пользователь
 * @param isDeleted
 *            удален ли пользователь
 */
public record InactiveUserResult(User user, boolean isInactive, boolean isDeleted) {
	@Builder
	public InactiveUserResult {
	}
}

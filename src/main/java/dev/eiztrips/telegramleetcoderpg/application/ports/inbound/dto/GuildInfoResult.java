package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import lombok.Builder;

import java.util.List;

/**
 * Информация о гильдии.
 *
 * @param isCreated
 *            дата создания
 * @param currentBoss
 *            текущий босс
 * @param users
 *            пользователи
 */
public record GuildInfoResult(Boolean isCreated, WeeklyBoss currentBoss, List<User> users) {
	@Builder
	public GuildInfoResult {
	}
}

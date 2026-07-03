package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.dto;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import lombok.Builder;

import java.util.List;

public record GuildInfoResult(Boolean isCreated, WeeklyBoss currentBoss, List<User> users) {
	@Builder
	public GuildInfoResult {
	}
}

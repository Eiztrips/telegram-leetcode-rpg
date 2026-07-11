package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto;

import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import lombok.Builder;

import java.util.List;

public record UserInfoResult(User user, Guild guild, List<Submission> submissions) {
	@Builder
	public UserInfoResult {
	}
}

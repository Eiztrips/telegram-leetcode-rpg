package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.guild;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.GuildInfoResult;

public interface GetOrCreateGuildInfoUseCase {
	GuildInfoResult getOrCreateGuild(Long chatId, Long telegramUserId);
}

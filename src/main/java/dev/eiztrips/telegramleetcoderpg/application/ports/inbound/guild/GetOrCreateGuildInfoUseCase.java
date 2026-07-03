package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.dto.GuildInfoResult;

public interface GetOrCreateGuildInfoUseCase {
	GuildInfoResult getOrCreateGuild(Long chatId, Long telegramUserId);
}

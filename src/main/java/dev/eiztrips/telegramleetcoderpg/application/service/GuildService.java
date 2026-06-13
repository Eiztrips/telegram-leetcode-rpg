package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.AddUserToGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.CreateGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.RemoveUserFromGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.RespawnWeeklyBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.ClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.ClientExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

/**
 * Сервис гильдии.
 */
public final class GuildService
		implements
			AddUserToGuildUseCase,
			CreateGuildUseCase,
			RemoveUserFromGuildUseCase,
			RespawnWeeklyBossUseCase {
	private final GuildRepositoryPort guildRepositoryPort;
	private final ClientPort clientPort;
	private final UserRepositoryPort userRepositoryPort;
	private final BossRepositoryPort bossRepositoryPort;

	public GuildService(GuildRepositoryPort guildRepositoryPort, ClientPort clientPort,
			UserRepositoryPort userRepositoryPort, BossRepositoryPort bossRepositoryPort) {
		this.guildRepositoryPort = guildRepositoryPort;
		this.clientPort = clientPort;
		this.userRepositoryPort = userRepositoryPort;
		this.bossRepositoryPort = bossRepositoryPort;
	}

	@Override
	public Guild create(Long chatId) {
		if (!clientPort.chatExists(chatId))
			throw new ClientExceptions.ChatNotFoundException(chatId);

		guildRepositoryPort.getGuildById(chatId).ifPresent(guild -> {
			throw new GuildExceptions.GuildAlreadyExists(chatId);
		});

		Guild guild = Guild.builder().id(chatId).build();
		guildRepositoryPort.save(guild);
		return guild;
	}

	@Override
	public void addUserToGuild(Long userId, Long guildId) {
		guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		User user = userRepositoryPort.getByTelegramId(userId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(userId));

		if (user.guildId() != null)
			throw new GuildExceptions.UserAlreadyExistsInGuild(userId);

		userRepositoryPort.save(user.withGuild(guildId));
	}

	@Override
	public void removeUserFromGuild(Long userId, Long guildId) {
		guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		User user = userRepositoryPort.getByTelegramId(userId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(userId));

		if (!user.guildId().equals(guildId))
			throw new GuildExceptions.UserNotFoundInGuild(userId);

		userRepositoryPort.save(user.withoutGuild());
	}

	@Override
	public void respawnWeeklyBoss(String name, int hp, Long guildId) {
		guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		WeeklyBoss boss = WeeklyBoss.builder().name(name).maxHp(hp).currentHp(hp).guildId(guildId).build();

		bossRepositoryPort.save(boss);
	}
}

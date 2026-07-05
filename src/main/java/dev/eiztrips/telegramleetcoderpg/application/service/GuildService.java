package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.*;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.dto.GuildInfoResult;
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

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

/**
 * Сервис гильдии.
 */
public final class GuildService
		implements
			AddUserToGuildUseCase,
			CreateGuildUseCase,
			RemoveUserFromGuildUseCase,
			RespawnWeeklyBossUseCase,
			GetOrCreateGuildInfoUseCase {
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

		WeeklyBoss currentBoss = bossRepositoryPort.save(bossRepositoryPort.getCurrentWeeklyBoss()
				.orElse(WeeklyBoss.builder().name("Убийца редиса").maxHp(100).currentHp(100).build()));

		Guild guild = Guild.builder().id(chatId).currentBossId(currentBoss.id()).build();

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

		if (user.guildId() == null || !user.guildId().equals(guildId))
			throw new GuildExceptions.UserNotFoundInGuild(userId);

		userRepositoryPort.save(user.withoutGuild());
	}

	@Override
	public void respawnWeeklyBoss(String name, int hp, Long guildId) {
		if (guildId != null) {
			respawnCurrentGuild(name, hp, guildId);
		} else {
			LocalDate now = LocalDate.now();
			LocalDate lastRespawn = bossRepositoryPort.getLastRespawnDate();

			WeekFields weekFields = WeekFields.of(Locale.getDefault());
			int currentWeek = now.get(weekFields.weekOfWeekBasedYear());
			int lastWeek = lastRespawn.get(weekFields.weekOfWeekBasedYear());
			int currentYear = now.get(weekFields.weekBasedYear());
			int lastYear = lastRespawn.get(weekFields.weekBasedYear());

			if (currentWeek == lastWeek && currentYear == lastYear) {
				return;
			}

			bossRepositoryPort.saveCurrentWeeklyBoss(WeeklyBoss.builder().name(name).maxHp(hp).build());

			guildRepositoryPort.getAllGuilds().forEach(g -> respawnCurrentGuild(name, hp, g.id()));

			bossRepositoryPort.saveLastRespawnDate(now);
		}
	}

	private void respawnCurrentGuild(String name, int hp, Long guildId) {
		Guild guild = guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		WeeklyBoss boss = WeeklyBoss.builder().name(name).maxHp(hp).currentHp(hp).build();

		boss = bossRepositoryPort.save(boss);
		guild = guild.withBoss(boss.id());

		guildRepositoryPort.save(guild);
	}

	@Override
	public GuildInfoResult getOrCreateGuild(Long chatId, Long telegramUserId) {
		boolean isCreated = false;

		if (guildRepositoryPort.getGuildById(chatId).isEmpty()) {
			create(chatId);
			addUserToGuild(telegramUserId, chatId);
			isCreated = true;
		}

		WeeklyBoss boss = guildRepositoryPort.getCurrentWeeklyBoss(chatId).orElse(null);
		List<User> users = userRepositoryPort.getUsersByGuildIdSortedByUserXpDesc(chatId);

		return GuildInfoResult.builder().isCreated(isCreated).currentBoss(boss).users(users).build();
	}
}

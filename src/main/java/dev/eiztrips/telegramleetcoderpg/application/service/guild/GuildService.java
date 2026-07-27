package dev.eiztrips.telegramleetcoderpg.application.service.guild;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.guild.*;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.GuildInfoResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.chat.ChatClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.ClientExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Сервис гильдии.
 */
@Service
public class GuildService
		implements
			AddUserToGuildUseCase,
			CreateGuildUseCase,
			RemoveUserFromGuildUseCase,
			RespawnWeeklyBossUseCase,
			GetOrCreateGuildInfoUseCase {
	private final GuildRepositoryPort guildRepositoryPort;
	private final ChatClientPort clientPort;
	private final UserRepositoryPort userRepositoryPort;
	private final BossRepositoryPort bossRepositoryPort;

	public GuildService(GuildRepositoryPort guildRepositoryPort, @Lazy ChatClientPort clientPort,
			UserRepositoryPort userRepositoryPort, BossRepositoryPort bossRepositoryPort) {
		this.guildRepositoryPort = guildRepositoryPort;
		this.clientPort = clientPort;
		this.userRepositoryPort = userRepositoryPort;
		this.bossRepositoryPort = bossRepositoryPort;
	}

	@Override
	@Transactional
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
	@Transactional
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
	@Transactional
	public void removeUserFromGuild(Long userId, Long guildId) {
		guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		User user = userRepositoryPort.getByTelegramId(userId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(userId));

		if (user.guildId() == null || !user.guildId().equals(guildId))
			throw new GuildExceptions.UserNotFoundInGuild(userId);

		userRepositoryPort.save(user.withoutGuild());

		if (userRepositoryPort.getUsersByGuildId(guildId).isEmpty()) {
			guildRepositoryPort.deleteById(guildId);
		}
	}

	@Override
	@Transactional
	public void respawnWeeklyBoss(String name, int hp, Long guildId) {
		if (guildId != null) {
			respawnCurrentGuild(name, hp, guildId);
		} else {
			bossRepositoryPort.saveCurrentWeeklyBoss(WeeklyBoss.builder().name(name).maxHp(hp).build());

			List<WeeklyBoss> guildBosses = new ArrayList<>();
			List<Guild> guilds = guildRepositoryPort.getAllGuilds();

			for (Guild g : guilds) {
				int guildBossHp = hp * userRepositoryPort.getUsersByGuildId(g.id()).size();
				guildBosses.add(WeeklyBoss.builder().name(name).maxHp(guildBossHp).currentHp(guildBossHp).build());
			}

			List<WeeklyBoss> bosses = bossRepositoryPort.saveAll(guildBosses);

			guilds = guilds.stream().map(guild -> {
				guild = guild.withBoss(bosses.getFirst().id());
				bosses.removeFirst();
				return guild;
			}).toList();

			guildRepositoryPort.saveAll(guilds);

			bossRepositoryPort.saveLastRespawnDate(LocalDate.now());
		}
	}

	private void respawnCurrentGuild(String name, int hp, Long guildId) {
		Guild guild = guildRepositoryPort.getGuildById(guildId)
				.orElseThrow(() -> new GuildExceptions.GuildNotFoundException(guildId));

		int membersCount = userRepositoryPort.getUsersByGuildId(guildId).size();
		hp = membersCount * hp;

		WeeklyBoss boss = WeeklyBoss.builder().name(name).maxHp(hp).currentHp(hp).build();

		boss = bossRepositoryPort.save(boss);
		guild = guild.withBoss(boss.id());

		guildRepositoryPort.save(guild);
	}

	@Override
	@Transactional
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

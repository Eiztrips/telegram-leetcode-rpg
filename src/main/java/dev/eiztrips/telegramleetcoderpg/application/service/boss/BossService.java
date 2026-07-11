package dev.eiztrips.telegramleetcoderpg.application.service.boss;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.boss.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис боя с боссом.
 */
@Service
public class BossService implements AttackBossUseCase {

	private final BossRepositoryPort bossRepositoryPort;
	private final UserRepositoryPort userRepositoryPort;
	private final GuildRepositoryPort guildRepositoryPort;

	public BossService(BossRepositoryPort bossRepositoryPort, UserRepositoryPort userRepositoryPort,
			GuildRepositoryPort guildRepositoryPort) {
		this.bossRepositoryPort = bossRepositoryPort;
		this.userRepositoryPort = userRepositoryPort;
		this.guildRepositoryPort = guildRepositoryPort;
	}

	@Override
	@Transactional
	public WeeklyBoss attackBoss(List<SubmissionData> submissionDataList, Long userId) {
		var userGuild = userRepositoryPort.getGuildByUserTelegramId(userId)
				.orElseThrow(UserExceptions.UserGuildNotFoundException::new);

		var guildBoss = guildRepositoryPort.getCurrentWeeklyBoss(userGuild.id())
				.orElseThrow(GuildExceptions.GuildBossNotFoundException::new);

		WeeklyBoss boss = bossRepositoryPort.getById(guildBoss.id())
				.orElseThrow(() -> new WeeklyBossExceptions.WeeklyBossNotFoundException(guildBoss.id()));

		var damage = 0;

		for (SubmissionData sd : submissionDataList)
			damage += Difficulty.valueOf(sd.taskDifficulty()).getReward();

		boss = boss.takeDamage(damage);

		bossRepositoryPort.save(boss);

		return boss;
	}
}

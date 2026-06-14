package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.ClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.service.BossFightService;
import dev.eiztrips.telegramleetcoderpg.application.service.GameProgressionService;
import dev.eiztrips.telegramleetcoderpg.application.service.GuildService;
import dev.eiztrips.telegramleetcoderpg.application.service.UserRegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ApplicationConfig {

	@Bean
	public AttackBossUseCase attackBossUseCase(BossRepositoryPort bossRepositoryPort) {
		return new BossFightService(bossRepositoryPort);
	}

	@Bean
	public CheckSubmissionsUseCase checkSubmissionsUseCase(UserRepositoryPort userRepositoryPort,
			LeetCodeClientPort leetCodeClientPort) {
		return new GameProgressionService(userRepositoryPort, leetCodeClientPort);
	}

	@Bean
	public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepositoryPort) {
		return new UserRegistrationService(userRepositoryPort);
	}

	@Bean
	public GuildService guildService(UserRepositoryPort userRepositoryPort, GuildRepositoryPort guildRepositoryPort,
			@Lazy ClientPort clientPort, BossRepositoryPort bossRepositoryPort) {
		return new GuildService(guildRepositoryPort, clientPort, userRepositoryPort, bossRepositoryPort);
	}
}

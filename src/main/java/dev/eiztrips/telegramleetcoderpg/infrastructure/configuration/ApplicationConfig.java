package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.ClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserCacheRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.service.BossService;
import dev.eiztrips.telegramleetcoderpg.application.service.GuildService;
import dev.eiztrips.telegramleetcoderpg.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ApplicationConfig {

	@Bean
	public BossService bossService(BossRepositoryPort bossRepositoryPort) {
		return new BossService(bossRepositoryPort);
	}

	@Bean
	public UserService userService(UserRepositoryPort userRepositoryPort, LeetCodeClientPort leetCodeClientPort,
			UserCacheRepositoryPort userCacheRepositoryPort) {
		return new UserService(userRepositoryPort, leetCodeClientPort, userCacheRepositoryPort);
	}

	@Bean
	public GuildService guildService(UserRepositoryPort userRepositoryPort, GuildRepositoryPort guildRepositoryPort,
			@Lazy ClientPort clientPort, BossRepositoryPort bossRepositoryPort) {
		return new GuildService(guildRepositoryPort, clientPort, userRepositoryPort, bossRepositoryPort);
	}
}

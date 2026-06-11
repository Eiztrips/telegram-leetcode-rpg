package dev.eiztrips.telegramleetcoderpg.infrastructure.configuration;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper.WeeklyBossMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository.SpringDataBossRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DataSeeder {
	@Bean
	CommandLineRunner seedBoss(SpringDataBossRepository springDataBossRepository, WeeklyBossMapper weeklyBossMapper) {
		return args -> {
			if (springDataBossRepository.count() > 0)
				return;

			WeeklyBossEntity wbe = weeklyBossMapper
					.toEntity(WeeklyBoss.builder().maxHp(100).currentHp(100).name("Фнюп").build());

			springDataBossRepository.save(wbe);
		};
	}
}

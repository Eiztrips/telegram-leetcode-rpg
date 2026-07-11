package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper.WeeklyBossMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.mapper.GuildMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.repository.SpringDataGuildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuildPersistenceAdapter implements GuildRepositoryPort {
	private final GuildMapper guildMapper;
	private final SpringDataGuildRepository guildRepository;
	private final WeeklyBossMapper weeklyBossMapper;
	private final BossRepositoryPort bossRepositoryPort;

	@Override
	public Guild save(Guild guild) {
		GuildEntity entity = guildMapper.toEntity(guild);

		if (entity.getCurrentBoss() != null) {
			long bossId = entity.getCurrentBoss().getId();
			WeeklyBossEntity wbe = bossRepositoryPort.getById(bossId).map(weeklyBossMapper::toEntity)
					.orElseThrow(() -> new WeeklyBossExceptions.WeeklyBossNotFoundException(bossId));
			entity.getCurrentBoss().setVersion(wbe.getVersion() == null ? 0L : wbe.getVersion());
		}

		return guildMapper.toDomain(guildRepository.save(entity));
	}

	@Override
	public Optional<Guild> getGuildById(Long guildId) {
		return guildRepository.findById(guildId).map(guildMapper::toDomain);
	}

	@Override
	public List<Guild> getAllGuilds() {
		return guildRepository.findAll().stream().map(guildMapper::toDomain).toList();
	}

	@Override
	public Optional<WeeklyBoss> getCurrentWeeklyBoss(Long guildId) {
		return guildRepository.findCurrentBossById(guildId).map(weeklyBossMapper::toDomain);
	}
}

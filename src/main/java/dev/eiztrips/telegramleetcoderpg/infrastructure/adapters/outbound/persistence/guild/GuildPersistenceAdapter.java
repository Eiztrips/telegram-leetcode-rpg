package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
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

	@Override
	public void save(Guild guild) {
		GuildEntity entity = guildMapper.toEntity(guild);
		guildRepository.save(entity);
	}

	@Override
	public Optional<Guild> getGuildById(Long guildId) {
		return guildRepository.findById(guildId).map(guildMapper::toDomain);
	}

	@Override
	public List<Guild> getAllGuilds() {
		return guildRepository.findAll().stream().map(guildMapper::toDomain).toList();
	}
}

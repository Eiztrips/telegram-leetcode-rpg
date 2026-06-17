package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.mapper;

import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuildMapper {
	@Mapping(target = "currentBoss.id", source = "currentBossId")
	GuildEntity toEntity(Guild domain);

	@Mapping(target = "currentBossId", source = "currentBoss.id")
	Guild toDomain(GuildEntity entity);
}

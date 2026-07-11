package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.mapper;

import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuildMapper {
	@Mapping(target = "currentBoss.id", source = "currentBossId")
	GuildEntity toEntity(Guild domain);
	List<GuildEntity> toEntityList(List<Guild> domainList);

	@Mapping(target = "currentBossId", source = "currentBoss.id")
	Guild toDomain(GuildEntity entity);
	List<Guild> toDomainList(List<GuildEntity> entityList);
}

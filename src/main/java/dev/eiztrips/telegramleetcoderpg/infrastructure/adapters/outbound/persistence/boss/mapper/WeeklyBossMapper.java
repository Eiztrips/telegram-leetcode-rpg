package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WeeklyBossMapper {
	WeeklyBossEntity toEntity(WeeklyBoss weeklyBoss);
	List<WeeklyBossEntity> toEntityList(List<WeeklyBoss> weeklyBosses);

	WeeklyBoss toDomain(WeeklyBossEntity entity);
	List<WeeklyBoss> toDomainList(List<WeeklyBossEntity> entities);
}

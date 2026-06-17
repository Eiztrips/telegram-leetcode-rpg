package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataGuildRepository extends JpaRepository<GuildEntity, Long> {
	@Query("SELECT g.currentBoss FROM GuildEntity g WHERE g.id = :id")
	Optional<WeeklyBossEntity> findCurrentBossById(@Param("id") Long guildId);
}

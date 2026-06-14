package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataGuildRepository extends JpaRepository<GuildEntity, Long> {
}

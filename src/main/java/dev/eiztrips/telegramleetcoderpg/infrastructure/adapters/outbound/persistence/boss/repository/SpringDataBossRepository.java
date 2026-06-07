package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataBossRepository extends JpaRepository<WeeklyBossEntity, Long> {
}

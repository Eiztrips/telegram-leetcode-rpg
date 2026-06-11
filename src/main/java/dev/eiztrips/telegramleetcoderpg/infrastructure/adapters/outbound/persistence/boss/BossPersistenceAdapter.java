package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper.WeeklyBossMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository.SpringDataBossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component("bossPersistenceAdapter")
@RequiredArgsConstructor
public class BossPersistenceAdapter implements BossRepositoryPort {

	private final SpringDataBossRepository bossRepository;
	private final WeeklyBossMapper weeklyBossMapper;

	@Override
	@Transactional
	public void save(WeeklyBoss boss) {
		if (boss.id() != null && boss.version() == null) {
			throw new GlobalExceptions.ArgumentEmptyException("version");
		}

		WeeklyBossEntity entity = weeklyBossMapper.toEntity(boss);
		bossRepository.save(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<WeeklyBoss> getById(Long id) {
		return bossRepository.findById(id).map(weeklyBossMapper::toDomain);
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper.WeeklyBossMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository.RedisDataBossRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository.SpringDataBossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component("bossPersistenceAdapter")
@RequiredArgsConstructor
public class BossPersistenceAdapter implements BossRepositoryPort {

	private final SpringDataBossRepository bossRepository;
	private final RedisDataBossRepository redisDataBossRepository;
	private final WeeklyBossMapper weeklyBossMapper;

	@Override
	@Transactional
	public WeeklyBoss save(WeeklyBoss boss) {
		if (boss.id() != null && boss.version() == null) {
			throw new GlobalExceptions.ArgumentEmptyException("version");
		}

		WeeklyBossEntity entity = weeklyBossMapper.toEntity(boss);
		return weeklyBossMapper.toDomain(bossRepository.save(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<WeeklyBoss> getById(Long id) {
		return bossRepository.findById(id).map(weeklyBossMapper::toDomain);
	}

	@Override
	public LocalDate getLastRespawnDate() {
		return redisDataBossRepository.getLastRespawnDate();
	}

	@Override
	public void saveLastRespawnDate(LocalDate date) {
		redisDataBossRepository.setLastRespawnDate(date);
	}

	@Override
	public Optional<WeeklyBoss> getCurrentWeeklyBoss() {
		return redisDataBossRepository.getCurrentWeeklyBoss();
	}

	@Override
	public void saveCurrentWeeklyBoss(WeeklyBoss boss) {
		redisDataBossRepository.saveCurrentWeeklyBoss(boss);
	}
}

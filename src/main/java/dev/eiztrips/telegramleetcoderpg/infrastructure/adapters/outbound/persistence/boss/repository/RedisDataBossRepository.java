package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisDataBossRepository {
	private final RedisTemplate<String, String> redisTemplate;

	private static final String BOSS_RESPAWN_KEY = "game:boss:last_respawn_date";
	private static final String BOSS_CURRENT_KEY = "game:boss:current";

	public LocalDate getLastRespawnDate() {
		String dateStr = redisTemplate.opsForValue().get(BOSS_RESPAWN_KEY);

		if (dateStr == null)
			return LocalDate.MIN;

		return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	public void setLastRespawnDate(LocalDate date) {
		if (date == null) {
			throw new GlobalExceptions.ArgumentEmptyException("date");
		}
		redisTemplate.opsForValue().set(BOSS_RESPAWN_KEY, date.format(DateTimeFormatter.ISO_LOCAL_DATE));
	}

	public Optional<WeeklyBoss> getCurrentWeeklyBoss() {
		String bossDetails = redisTemplate.opsForValue().get(BOSS_CURRENT_KEY);
		if (bossDetails == null || bossDetails.isBlank())
			return Optional.empty();

		String[] bossDetailsArray = bossDetails.split(" ");

		if (bossDetails.split(" ").length != 3)
			return Optional.empty();

		String preName = bossDetailsArray[0];
		String name = bossDetailsArray[1];
		String hp = bossDetailsArray[2];

		WeeklyBoss boss = WeeklyBoss.builder().name(preName + " " + name).maxHp(Integer.parseInt(hp))
				.currentHp(Integer.parseInt(hp)).build();

		return Optional.of(boss);
	}

	public void saveCurrentWeeklyBoss(WeeklyBoss boss) {
		redisTemplate.opsForValue().set(BOSS_CURRENT_KEY, boss.name() + " " + boss.maxHp());
	}
}

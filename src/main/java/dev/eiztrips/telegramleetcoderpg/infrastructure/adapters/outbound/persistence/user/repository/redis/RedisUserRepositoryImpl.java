package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.redis;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.dto.UserRegistrationCacheData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisUserRepositoryImpl implements RedisUserRepository {
	private final StringRedisTemplate redisTemplate;
	private static final String KEY = "registration_cache:";

	@Override
	public void save(UserRegistrationCacheData data) {
		String key = KEY + data.chatId().toString();
		String val = data.token();

		redisTemplate.opsForValue().set(key, val, data.ttl().toMinutes(), TimeUnit.MINUTES);
	}

	@Override
	public Optional<UserRegistrationCacheData> findByChatId(Long telegramChatId) {
		String val = redisTemplate.opsForValue().get(KEY + telegramChatId.toString());

		if (val == null)
			return Optional.empty();

		String[] parts = val.split(":", 3);

		if (parts.length < 3)
			return Optional.empty();

		return Optional.of(UserRegistrationCacheData.builder().token(val).leetcodeUserName(parts[2])
				.chatId(telegramChatId).build());
	}

	@Override
	public void remove(Long telegramChatId) {
		String key = KEY + telegramChatId.toString();
		redisTemplate.delete(key);
	}
}

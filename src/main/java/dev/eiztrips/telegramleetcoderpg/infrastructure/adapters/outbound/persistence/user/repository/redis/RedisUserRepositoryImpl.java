package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.redis;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.UserRegistrationCacheData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisUserRepositoryImpl implements RedisUserRepository {
	private final StringRedisTemplate redisTemplate;
	private static final String REGISTRATION_KEY = "registration_cache:";
	private static final String INACTIVE_KEY = "inactive:";

	@Override
	public void saveRegistrationCacheData(UserRegistrationCacheData data) {
		String key = REGISTRATION_KEY + data.chatId().toString();
		String val = data.token();

		redisTemplate.opsForValue().set(key, val, data.ttl().toMinutes(), TimeUnit.MINUTES);
	}

	@Override
	public Optional<UserRegistrationCacheData> findRegistrationCacheDataByChatId(Long telegramChatId) {
		String val = redisTemplate.opsForValue().get(REGISTRATION_KEY + telegramChatId.toString());

		if (val == null)
			return Optional.empty();

		String[] parts = val.split(":", 3);

		if (parts.length < 3)
			return Optional.empty();

		return Optional.of(UserRegistrationCacheData.builder().token(val).leetcodeUserName(parts[2])
				.chatId(telegramChatId).build());
	}

	@Override
	public void removeRegistrationCacheData(Long telegramChatId) {
		String key = REGISTRATION_KEY + telegramChatId.toString();
		redisTemplate.delete(key);
	}

	@Override
	public void saveUserInactive(Long telegramChatId) {
		redisTemplate.opsForSet().add(INACTIVE_KEY, telegramChatId.toString());
	}

	@Override
	public void deleteUserInactive(Long telegramChatId) {
		redisTemplate.opsForSet().remove(INACTIVE_KEY, telegramChatId.toString());
	}

	@Override
	public Boolean checkUserInactive(Long telegramChatId) {
		return redisTemplate.opsForSet().isMember(INACTIVE_KEY, telegramChatId.toString());
	}
}

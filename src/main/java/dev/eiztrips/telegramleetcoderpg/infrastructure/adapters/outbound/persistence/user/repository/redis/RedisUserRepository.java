package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.redis;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.dto.UserRegistrationCacheData;

import java.util.Optional;

public interface RedisUserRepository {
	void save(UserRegistrationCacheData data);

	Optional<UserRegistrationCacheData> findByChatId(Long telegramChatId);

	void remove(Long telegramChatId);
}

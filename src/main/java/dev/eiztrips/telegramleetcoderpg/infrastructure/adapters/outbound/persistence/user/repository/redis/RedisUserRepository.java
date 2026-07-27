package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.redis;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.UserRegistrationCacheData;

import java.util.Optional;

public interface RedisUserRepository {
	void saveRegistrationCacheData(UserRegistrationCacheData data);

	Optional<UserRegistrationCacheData> findRegistrationCacheDataByChatId(Long telegramChatId);

	void removeRegistrationCacheData(Long telegramChatId);

	void saveUserInactive(Long telegramChatId);

	void deleteUserInactive(Long telegramChatId);

	Boolean checkUserInactive(Long telegramChatId);
}

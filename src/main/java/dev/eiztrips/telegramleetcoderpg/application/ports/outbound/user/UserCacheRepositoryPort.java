package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.dto.UserRegistrationCacheData;

import java.util.Optional;

/**
 * Порт репозитория кэша пользователей
 */
public interface UserCacheRepositoryPort {
	/**
	 * Сохранить токен регистрации пользователя
	 *
	 * @param data
	 *            данные токена
	 */
	void saveRegistrationToken(UserRegistrationCacheData data);

	/**
	 * Получить токен регистрации пользователя
	 *
	 * @param telegramChatId
	 *            id пользователя телеграм
	 * @return токен, если есть
	 */
	Optional<UserRegistrationCacheData> getRegistrationCache(Long telegramChatId);

	/**
	 * Удалить сессию пользователя
	 *
	 * @param telegramChatId
	 *            id пользователя телеграмм
	 */
	void removeRegistrationCache(Long telegramChatId);
}

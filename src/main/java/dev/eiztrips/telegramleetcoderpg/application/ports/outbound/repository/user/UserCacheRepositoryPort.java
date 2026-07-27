package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.UserRegistrationCacheData;

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

	/**
	 * Пометить пользователя как неактивного
	 *
	 * @param telegramChatId
	 *            id пользователя телеграм
	 */
	void makeUserInactive(Long telegramChatId);

	/**
	 * Убрать пометку неактивности с пользователя
	 *
	 * @param telegramChatId
	 *            id пользователя телеграм
	 */
	void makeUserActive(Long telegramChatId);

	/**
	 * Проверить активность пользователя
	 *
	 * @param telegramChatId
	 *            id пользователя телеграм
	 * @return true если пользователь инактивен
	 */
	boolean checkUserInactive(Long telegramChatId);
}

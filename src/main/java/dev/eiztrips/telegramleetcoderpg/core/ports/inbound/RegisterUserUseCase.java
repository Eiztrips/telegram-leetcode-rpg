package dev.eiztrips.telegramleetcoderpg.core.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions.UserAlreadyExistsException;

/**
 * Сценарий использования: регистрация пользователя.
 */
public interface RegisterUserUseCase {
	/**
	 * Зарегистрировать пользователя.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @param name
	 *            имя
	 * @param leetcodeURL
	 *            ссылка на профиль leetcode
	 * @return зарегистрированный пользователь
	 *
	 * @throws UserAlreadyExistsException
	 *             пользователь уже был зарегистрирован
	 */
	User registerUser(Long userTelegramId, String name, String leetcodeURL);
}

package dev.eiztrips.telegramleetcoderpg.application.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserAlreadyExistsException;

/**
 * Сценарий использования: регистрация пользователя.
 */
public interface RegisterUserUseCase {
	/**
	 * Зарегистрировать пользователя.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @param leetcodeUsername
	 *            имя пользователя LeetCode
	 * @return зарегистрированный пользователь
	 *
	 * @throws UserAlreadyExistsException
	 *             пользователь уже был зарегистрирован
	 */
	User registerUser(Long userTelegramId, String leetcodeUsername);
}

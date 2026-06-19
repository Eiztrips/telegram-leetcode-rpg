package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.TelegramIdAlreadyExistsException;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.LeetcodeUsernameAlreadyExistsException;

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
	 * @throws TelegramIdAlreadyExistsException
	 *             пользователь c таким id уже зарегистрирован
	 * @throws LeetcodeUsernameAlreadyExistsException
	 *             пользователь с таким leetcode никнеймом уже зарегистрирован
	 */
	User registerUser(Long userTelegramId, String leetcodeUsername);
}

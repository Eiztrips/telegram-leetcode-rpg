package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserAlreadyExistsException;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.LeetcodeUsernameAlreadyExistsException;

/**
 * Сценарий использования: регистрация пользователя.
 */
public interface RegisterUserUseCase {
	/**
	 * Начать процесс регистрации.
	 *
	 * @param userTelegramId
	 *            telegramId пользователя
	 * @param leetcodeUsername
	 *            имя пользователя LeetCode
	 * @return токен
	 *
	 * @throws UserAlreadyExistsException
	 *             пользователь c таким id уже зарегистрирован
	 * @throws LeetcodeUsernameAlreadyExistsException
	 *             пользователь с таким leetcode никнеймом уже зарегистрирован
	 */
	String startUserRegistration(Long userTelegramId, String leetcodeUsername);

	/**
	 * Зарегистрировать пользователя.
	 *
	 * @param userTelegramId
	 *            telegramId пользователя
	 * @return зарегистрированный пользователь
	 *
	 * @throws UserAlreadyExistsException
	 *             пользователь c таким id уже зарегистрирован
	 * @throws LeetcodeUsernameAlreadyExistsException
	 *             пользователь с таким leetcode никнеймом уже зарегистрирован
	 */
	User completeUserRegistration(Long userTelegramId);

	/**
	 * Создание пользователя.
	 *
	 * @param userTelegramId
	 *            telegramId пользователя
	 * @param leetcodeUsername
	 *            имя пользователя leetCode
	 * @return созданный пользователь
	 */
	User createUser(Long userTelegramId, String leetcodeUsername);

	/**
	 * Сгенерировать токен регистрации.
	 *
	 * @param userTelegramId
	 *            telegramId пользователя
	 * @param leetcodeUsername
	 *            имя пользователя LeetCode
	 * @return токен
	 */
	String generateRegistrationToken(Long userTelegramId, String leetcodeUsername);
}

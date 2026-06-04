package dev.eiztrips.telegramleetcoderpg.core.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions.UserNotFoundException;
import dev.eiztrips.telegramleetcoderpg.core.domain.exception.TaskExceptions.TaskCheckRateLimitException;

/**
 * Сценарий использования: проверка решений.
 */
public interface CheckSubmissionsUseCase {
	/**
	 * Проверить сегодняшние отправленные решения.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return true, если есть новые решения, иначе false
	 *
	 * @throws UserNotFoundException
	 *             Если пользователь не существует
	 * @throws TaskCheckRateLimitException
	 *             Пользователь уже использовал submission за последние 24 часа
	 */
	boolean checkTodaySubmissions(Long userTelegramId);
}

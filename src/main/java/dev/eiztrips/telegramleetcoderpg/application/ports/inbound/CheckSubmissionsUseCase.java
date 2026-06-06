package dev.eiztrips.telegramleetcoderpg.application.ports.inbound;

import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserNotFoundException;
import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions.SubmissionCheckRateLimitException;

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
	 * @throws SubmissionCheckRateLimitException
	 *             Пользователь уже использовал submission за последние 24 часа
	 */
	boolean checkTodaySubmissions(Long userTelegramId);
}

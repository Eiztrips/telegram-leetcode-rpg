package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserNotFoundException;
import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions.SubmissionCheckRateLimitException;

import java.util.List;

/**
 * Сценарий использования: проверка решений.
 */
public interface CheckSubmissionsUseCase {
	/**
	 * Проверить сегодняшние отправленные решения.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return список новых Submissions
	 *
	 * @throws UserNotFoundException
	 *             Если пользователь не существует
	 * @throws SubmissionCheckRateLimitException
	 *             Пользователь уже использовал submission за последние 24 часа
	 */
	List<SubmissionData> checkTodaySubmissions(Long userTelegramId);
}

package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;

import java.util.*;

/**
 * Порт репозитория пользователей.
 */
public interface UserRepositoryPort {
	/**
	 * Сохранить пользователя.
	 *
	 * @param user
	 *            пользователь
	 */
	void save(User user);

	/**
	 * Добавить решенный submission.
	 *
	 * @param data
	 *            Data сабмишена с литкода
	 */
	void addSubmissions(Long userTelegramId, List<SubmissionData> data);

	/**
	 * Получить пользователя по telegram telegramId.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return пользователь, если существует
	 */
	Optional<User> getByTelegramId(Long userTelegramId);

	/**
	 * Получить все submissions пользователя за последнюю неделю
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return список submissions
	 */
	List<SubmissionData> getSubmissionsLastWeek(Long userTelegramId);

	/**
	 * Проверить существование пользователя по telegram telegramId.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return true, если существует, иначе false
	 */
	boolean existsByTelegramId(Long userTelegramId);
}

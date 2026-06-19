package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user;

import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
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
	 * Получить пользователя по telegramId.
	 *
	 * @param userTelegramId
	 *            telegram пользователя
	 * @return пользователь, если существует
	 */
	Optional<User> getByTelegramId(Long userTelegramId);

	/**
	 * Получить пользователя по никнейму leetcode
	 *
	 * @param username
	 *            никней LeetCode пользователя
	 * @return пользователь, если существует
	 */
	Optional<User> getByLeetCodeUsername(String username);

	/**
	 * Получить гильдию пользователя по telegram id.
	 *
	 * @param userTelegramId
	 *            telegramId пользователя
	 *
	 * @return гильдия, если существует
	 */
	Optional<Guild> getGuildByUserTelegramId(Long userTelegramId);

	/**
	 * Получить все submissions пользователя за последнюю неделю
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return список submissions
	 */
	List<SubmissionData> getSubmissionsLastWeek(Long userTelegramId);
}

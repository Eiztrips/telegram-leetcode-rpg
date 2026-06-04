package dev.eiztrips.telegramleetcoderpg.core.ports.outbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Task;

import java.util.List;

/**
 * Порт клиента LeetCode API.
 */
public interface LeetCodeClientPort {
	/**
	 * Получить сегодняшние отправленные решения.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return список сегодняшних решений
	 */
	List<Task> getTodaySubmissions(Long userTelegramId);
}

package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;

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
	List<SubmissionData> getTodaySubmissions(Long userTelegramId);
}

package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.leetcode;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;

import java.util.List;

/**
 * Порт клиента LeetCode API.
 */
public interface LeetCodeClientPort {
	/**
	 * Получить сегодняшние отправленные решения.
	 *
	 * @param leetcodeUsername
	 *            имя пользователя на LeetCode
	 * @return список сегодняшних решений
	 */
	List<SubmissionData> getTodaySubmissions(String leetcodeUsername);

	/**
	 * Получить описание профиля пользователя.
	 *
	 * @param leetcodeUsername
	 *            имя пользователя на LeetCode
	 * @return описание профиля
	 */
	String getBio(String leetcodeUsername);
}

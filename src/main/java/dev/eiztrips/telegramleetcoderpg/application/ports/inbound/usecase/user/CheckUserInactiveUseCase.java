package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.InactiveUserResult;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

import java.util.List;

/**
 * Сценарий использования: проверить инактив пользователя.
 */
public interface CheckUserInactiveUseCase {

	/**
	 * Проверить активность пользователя за последнюю неделю.
	 *
	 * @param user
	 *            пользователь
	 * @param removeTimeDays
	 *            Время в днях до удаления (null если не удалять)
	 * @param alarmTimeDays
	 *            Время в днях до оповещения (null если не нужно оповещать)
	 * @return true если пользователь инактивен
	 */
	InactiveUserResult checkUserInactive(User user, Integer removeTimeDays, Integer alarmTimeDays);

	/**
	 * Проверить активность всех пользователей за последнюю неделю.
	 *
	 * @param removeTimeDays
	 *            Время в днях до удаления (null если не удалять)
	 * @param alarmTimeDays
	 *            Время в днях до оповещения (null если не нужно оповещать)
	 * @return список инактивных пользователей
	 */
	List<InactiveUserResult> checkAllUsersInactive(Integer removeTimeDays, Integer alarmTimeDays);
}

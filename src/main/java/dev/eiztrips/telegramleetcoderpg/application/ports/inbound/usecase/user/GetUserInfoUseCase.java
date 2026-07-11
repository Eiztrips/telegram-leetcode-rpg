package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;

/**
 * Сценарий использования: получить информацию о пользователе.
 */
public interface GetUserInfoUseCase {
	/**
	 * Получеть информацию о пользователе
	 * 
	 * @param leetcodeUsername
	 *            ник пользователя на литкоде
	 * @return информация
	 */
	UserInfoResult getUserInfo(String leetcodeUsername);
}

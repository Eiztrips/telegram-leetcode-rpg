package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto;

import lombok.Builder;

import java.time.Duration;
import java.util.Objects;

/**
 * DTO регистрации пользователя
 *
 * @param token
 *            токен регистрации
 * @param leetcodeUserName
 *            никнейм пользователя литкод
 * @param chatId
 *            id чата телеграм
 * @param ttl
 *            срок жизни сессии
 */
public record UserRegistrationCacheData(String token, String leetcodeUserName, Long chatId, Duration ttl) {
	@Builder
	public UserRegistrationCacheData {
		Objects.requireNonNull(token, "Токен не можт быть пустой");
		Objects.requireNonNull(chatId, "chatId не можт быть пустой");
		Objects.requireNonNull(leetcodeUserName, "Leetcode никнейм не можт быть пустой");
	}
}

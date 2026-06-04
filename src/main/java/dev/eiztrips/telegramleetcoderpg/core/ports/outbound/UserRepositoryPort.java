package dev.eiztrips.telegramleetcoderpg.core.ports.outbound;

import dev.eiztrips.telegramleetcoderpg.core.domain.model.user.User;

import java.util.Optional;

/**
 * Порт репозитория пользователей.
 */
public interface UserRepositoryPort {
	/**
	 * Сохранить пользователя.
	 *
	 * @param user
	 *            пользователь
	 * @return сохраненный пользователь
	 */
	User save(User user);

	/**
	 * Получить пользователя по telegram telegramId.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return пользователь, если существует
	 */
	Optional<User> getByTelegramId(Long userTelegramId);

	/**
	 * Проверить существование пользователя по telegram telegramId.
	 *
	 * @param userTelegramId
	 *            telegram telegramId пользователя
	 * @return true, если существует, иначе false
	 */
	boolean existsByTelegramId(Long userTelegramId);
}

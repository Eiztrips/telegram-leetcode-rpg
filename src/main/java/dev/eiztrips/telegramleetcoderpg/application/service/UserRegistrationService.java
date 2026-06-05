package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions.*;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.core.ports.inbound.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.core.ports.outbound.UserRepositoryPort;

/**
 * Сервис регистрации пользователей.
 */
public final class UserRegistrationService implements RegisterUserUseCase {

	private final UserRepositoryPort userRepository;

	/**
	 * Создает новый сервис регистрации пользователей.
	 *
	 * @param userRepository
	 *            репозиторий пользователей
	 */
	public UserRegistrationService(UserRepositoryPort userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User registerUser(Long userTelegramId, String name, String leetcodeURL) {
		if (userRepository.existsByTelegramId(userTelegramId)) {
			throw new UserAlreadyExistsException();
		}

		User user = new User(userTelegramId, name, leetcodeURL, 0, null, null);

		return userRepository.save(user);
	}
}

package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;

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

		User user = User.builder().telegramId(userTelegramId).username(name).leetcodeURL(leetcodeURL).xp(0)
				.lastCheckTime(null).build();

		userRepository.save(user);

		return user;
	}
}

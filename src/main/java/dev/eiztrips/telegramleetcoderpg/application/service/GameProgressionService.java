package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Task;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.core.ports.inbound.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.core.ports.outbound.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.core.ports.outbound.UserRepositoryPort;

import java.util.List;
import java.util.Set;

/**
 * Сервис игрового прогресса.
 */
public final class GameProgressionService implements CheckSubmissionsUseCase {

	private final UserRepositoryPort userRepository;
	private final LeetCodeClientPort leetCodeClient;

	/**
	 * Создает новый сервис игрового прогресса.
	 *
	 * @param userRepository
	 *            репозиторий пользователей
	 * @param leetCodeClient
	 *            клиент LeetCode
	 */
	public GameProgressionService(UserRepositoryPort userRepository, LeetCodeClientPort leetCodeClient) {
		this.userRepository = userRepository;
		this.leetCodeClient = leetCodeClient;
	}

	@Override
	public boolean checkTodaySubmissions(Long userTelegramId) {
		User user = userRepository.getByTelegramId(userTelegramId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(userTelegramId));

		user.validateCheckRateLimit();

		List<Task> todaySubmittedTasks = leetCodeClient.getTodaySubmissions(userTelegramId);
		Set<Task> userTasksLastWeek = user.getCompletedTasksLastWeek();

		boolean hasNewSubmissions = false;

		for (Task task : todaySubmittedTasks) {
			if (!userTasksLastWeek.contains(task)) {
				user = user.completeTask(task);
				hasNewSubmissions = true;
			}
		}

		user = user.withLastCheckTime();

		userRepository.save(user);

		return hasNewSubmissions;
	}
}

package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserCacheRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.dto.UserRegistrationCacheData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис регистрации пользователей.
 */
public final class UserService implements RegisterUserUseCase, CheckSubmissionsUseCase {

	private final UserRepositoryPort userRepository;
	private final LeetCodeClientPort leetCodeClient;
	private final UserCacheRepositoryPort userCacheRepositoryPort;

	public UserService(UserRepositoryPort userRepository, LeetCodeClientPort leetCodeClientPort,
			UserCacheRepositoryPort userCacheRepositoryPort) {
		this.userRepository = userRepository;
		this.leetCodeClient = leetCodeClientPort;
		this.userCacheRepositoryPort = userCacheRepositoryPort;
	}

	@Override
	public String startUserRegistration(Long userTelegramId, String leetcodeUsername) {
		validateUserUniqueness(userTelegramId, leetcodeUsername);

		userCacheRepositoryPort.getRegistrationCache(userTelegramId)
				.ifPresent(_ -> userCacheRepositoryPort.removeRegistrationCache(userTelegramId));

		return generateRegistrationToken(userTelegramId, leetcodeUsername);
	}

	@Override
	public User completeUserRegistration(Long userTelegramId) {
		validateUserUniqueness(userTelegramId, null);

		Optional<UserRegistrationCacheData> cache = userCacheRepositoryPort.getRegistrationCache(userTelegramId);

		if (cache.isEmpty())
			throw new RegistrationTokenExpiredException();

		String leetcodeUsername = cache.get().leetcodeUserName();
		validateUserUniqueness(null, leetcodeUsername);

		String bio = leetCodeClient.getBio(leetcodeUsername);
		if (!bio.equals(cache.get().token()))
			throw new BadRegistrationTokenException();

		return createUser(userTelegramId, leetcodeUsername);
	}

	@Override
	public User createUser(Long userTelegramId, String leetcodeUsername) {
		validateUserUniqueness(userTelegramId, leetcodeUsername);

		var user = User.builder().telegramId(userTelegramId).leetcodeUsername(leetcodeUsername).xp(0).lastCheckTime(null)
				.build();

		userRepository.save(user);
		return user;
	}

	@Override
	public String generateRegistrationToken(Long userTelegramId, String leetcodeUsername) {
		var token = UUID.randomUUID().toString() + ':' + userTelegramId.toString() + ':' + leetcodeUsername;
		var ttl = Duration.ofMinutes(15);
		var data = UserRegistrationCacheData.builder().chatId(userTelegramId).token(token)
				.leetcodeUserName(leetcodeUsername).ttl(ttl).build();

		userCacheRepositoryPort.saveRegistrationToken(data);

		return token;
	}

	private void validateUserUniqueness(Long userTelegramId, String leetcodeUsername) {
		if (userTelegramId != null)
			userRepository.getByTelegramId(userTelegramId).ifPresent(user -> {
				throw new UserAlreadyExistsException();
			});

		if (leetcodeUsername != null)
			userRepository.getByLeetCodeUsername(leetcodeUsername).ifPresent(user -> {
				throw new LeetcodeUsernameAlreadyExistsException();
			});
	}

	@Override
	public List<SubmissionData> checkTodaySubmissions(Long userTelegramId) {
		User user = userRepository.getByTelegramId(userTelegramId)
				.orElseThrow(UserExceptions.UserNotFoundException::new);

		user.validateCheckRateLimit();

		List<SubmissionData> todaySubmissions = leetCodeClient.getTodaySubmissions(user.leetcodeUsername());
		List<SubmissionData> lastWeekSubmissions = userRepository.getSubmissionsLastWeek(userTelegramId);

		var tasksLastWeek = lastWeekSubmissions.stream().map(SubmissionData::taskSlug)
				.collect(Collectors.toUnmodifiableSet());

		var newSubmissions = todaySubmissions.stream().filter(sub -> !tasksLastWeek.contains(sub.taskSlug())).toList();

		if (newSubmissions.isEmpty()) {
			userRepository.save(user.withLastCheckTime());
			return new ArrayList<>();
		}

		User updatedUser = user.takeRewardForSolveTask(newSubmissions.stream().map(this::toSubmission).toList());

		userRepository.addSubmissions(userTelegramId, newSubmissions);
		userRepository.save(updatedUser.withLastCheckTime());

		return newSubmissions;
	}

	private Submission toSubmission(SubmissionData data) {
		return Submission.builder().taskSlug(data.taskSlug())
				.taskDifficulty(Difficulty.valueOf(data.taskDifficulty().toUpperCase())).completedAt(data.completedAt())
				.build();
	}
}

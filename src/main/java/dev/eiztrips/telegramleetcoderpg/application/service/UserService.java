package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис регистрации пользователей.
 */
public final class UserService implements RegisterUserUseCase, CheckSubmissionsUseCase {

	private final UserRepositoryPort userRepository;
	private final LeetCodeClientPort leetCodeClient;

	public UserService(UserRepositoryPort userRepository, LeetCodeClientPort leetCodeClientPort) {
		this.userRepository = userRepository;
		this.leetCodeClient = leetCodeClientPort;
	}

	@Override
	public User registerUser(Long userTelegramId, String leetcodeUsername) {
		userRepository.getByTelegramId(userTelegramId).ifPresent(user -> {
			throw new TelegramIdAlreadyExistsException();
		});

		userRepository.getByLeetCodeUsername(leetcodeUsername).ifPresent(user -> {
			throw new LeetcodeUsernameAlreadyExistsException();
		});

		User user = User.builder().telegramId(userTelegramId).leetcodeUsername(leetcodeUsername).xp(0)
				.lastCheckTime(null).build();

		// todo: добавить валидацию LeetCode пользователя

		userRepository.save(user);

		return user;
	}

	@Override
	public List<SubmissionData> checkTodaySubmissions(Long userTelegramId) {
		User user = userRepository.getByTelegramId(userTelegramId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(userTelegramId));

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

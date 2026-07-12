package dev.eiztrips.telegramleetcoderpg.application.service.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.GetUserInfoUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserCacheRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.UserRegistrationCacheData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.*;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис регистрации пользователей.
 */
@Slf4j
@Service
public class UserService implements RegisterUserUseCase, CheckSubmissionsUseCase, GetUserInfoUseCase {

	private final UserRepositoryPort userRepository;
	private final LeetCodeClientPort leetCodeClient;
	private final UserCacheRepositoryPort userCacheRepositoryPort;
	private final TransactionTemplate transactionTemplate;

	public UserService(UserRepositoryPort userRepository, LeetCodeClientPort leetCodeClientPort,
			UserCacheRepositoryPort userCacheRepositoryPort, TransactionTemplate transactionTemplate) {
		this.userRepository = userRepository;
		this.leetCodeClient = leetCodeClientPort;
		this.userCacheRepositoryPort = userCacheRepositoryPort;
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	@Transactional
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

		return transactionTemplate.execute(status -> createUser(userTelegramId, leetcodeUsername));
	}

	@Override
	public User createUser(Long userTelegramId, String leetcodeUsername) {
		validateUserUniqueness(userTelegramId, leetcodeUsername);

		var user = User.builder().telegramId(userTelegramId).leetcodeUsername(leetcodeUsername).xp(0)
				.lastCheckTime(null).build();

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

		if (newSubmissions.isEmpty())
			return new ArrayList<>();

		List<Submission> submissions = newSubmissions.stream().map(this::toSubmission).toList();

		User updatedUser = user.takeRewardForSolveTask(submissions);

		return transactionTemplate.execute(status -> {
			userRepository.addSubmissions(userTelegramId, submissions);
			userRepository.save(updatedUser.withLastCheckTime());
			return newSubmissions;
		});
	}

	@Override
	@Transactional(readOnly = true)
	public UserInfoResult getUserInfo(String leetcodeUsername) {
		User user = userRepository.getByLeetCodeUsername(leetcodeUsername).orElseThrow(UserNotFoundException::new);

		Guild guild = userRepository.getGuildByUserTelegramId(user.telegramId()).orElse(null);

		List<SubmissionData> submissions = userRepository.getSubmissionsLastWeek(user.telegramId());

		return UserInfoResult.builder().user(user).guild(guild)
				.submissions(submissions.stream().map(this::toSubmission).toList()).build();
	}

	private Submission toSubmission(SubmissionData data) {
		return Submission.builder().submissionId(data.submissionId()).taskTitle(data.taskTitle())
				.taskSlug(data.taskSlug()).taskDifficulty(Difficulty.valueOf(data.taskDifficulty().toUpperCase()))
				.completedAt(data.completedAt()).build();
	}
}

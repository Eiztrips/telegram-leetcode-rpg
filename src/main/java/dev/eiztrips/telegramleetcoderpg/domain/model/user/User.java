package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions;
import lombok.Builder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Доменная модель пользователя.
 */
public record User(Long telegramId, String leetcodeUsername, int xp, Instant lastCheckTime) {

	@Builder
	public User {
		if (telegramId == null)
			throw new GlobalExceptions.ArgumentEmptyException("telegramId");
		if (leetcodeUsername == null || leetcodeUsername.isBlank())
			throw new GlobalExceptions.ArgumentEmptyException("leetcodeUsername");

		lastCheckTime = (lastCheckTime == null) ? Instant.now() : lastCheckTime;
	}

	/**
	 * Награждение за выполненные задачи.
	 *
	 * @param submissions
	 *            решенные задачи
	 * @return обновленный пользователь
	 */
	public User takeRewardForSolveTask(List<Submission> submissions) {
		int newXp = xp;

		for (Submission submission : submissions) {
			newXp += submission.getReward();
		}

		return new User(this.telegramId, this.leetcodeUsername, newXp, this.lastCheckTime);
	}

	/**
	 * Обновить последний момент проверки.
	 *
	 * @return обновленный пользователь
	 */
	public User withLastCheckTime() {
		return new User(this.telegramId, this.leetcodeUsername, this.xp, Instant.now());
	}

	/**
	 * Проверка отправлений за последние 24 часа.
	 */
	public void validateCheckRateLimit() {
		if (this.lastCheckTime.isAfter(Instant.now().minus(Duration.ofDays(1)))) {
			throw new SubmissionExceptions.SubmissionCheckRateLimitException(lastCheckTime);
		}
	}
}

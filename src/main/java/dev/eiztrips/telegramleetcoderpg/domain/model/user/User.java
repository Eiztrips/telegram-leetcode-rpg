package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Доменная модель пользователя.
 */
public record User(Long telegramId, String username, String leetcodeURL, int xp, Instant lastCheckTime) {
	private static final Pattern LEETCODE_PATTERN = Pattern
			.compile("^(https?://)?(www\\.)?leetcode\\.com/(u/)?([a-zA-Z0-9_-]+)/?$");

	/**
	 * Инициализация пользователя.
	 *
	 * @param telegramId
	 *            идентификатор
	 * @param username
	 *            имя пользователя
	 * @param leetcodeURL
	 *            ссылка на LeetCode профиль
	 * @param xp
	 *            опыт
	 * @param lastCheckTime
	 *            время последней проверки (по умолчанию: сейчас)
	 */
	public User {
		if (username == null || username.isBlank()) {
			throw new UserExceptions.ArgumentEmptyException("username");
		}

		if (leetcodeURL == null || leetcodeURL.isBlank()) {
			throw new UserExceptions.ArgumentEmptyException("LeetCode url");
		}

		if (!LEETCODE_PATTERN.matcher(leetcodeURL.trim()).matches())
			throw new UserExceptions.InvalidLeetCodeUrlException(leetcodeURL.trim());

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

		return new User(this.telegramId, this.username, this.leetcodeURL, newXp, this.lastCheckTime);
	}

	/**
	 * Обновить последний момент проверки.
	 *
	 * @return обновленный пользователь
	 */
	public User withLastCheckTime() {
		return new User(this.telegramId, this.username, this.leetcodeURL, this.xp, Instant.now());
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

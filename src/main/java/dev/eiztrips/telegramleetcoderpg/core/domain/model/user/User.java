package dev.eiztrips.telegramleetcoderpg.core.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.TaskExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Доменная модель пользователя.
 */
public record User(Long telegramId, String username, String leetcodeURL, int xp, List<Submission> submissions,
		Instant lastCheckTime) {
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
	 * @param submissions
	 *            отправленные решения (по умолчанию: [])
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

		submissions = (submissions == null) ? List.of() : List.copyOf(submissions);
		lastCheckTime = (lastCheckTime == null) ? Instant.now() : lastCheckTime;
	}

	/**
	 * Награждение за выполненную задачу.
	 *
	 * @param task
	 *            решенная задача
	 * @return обновленный пользователь
	 */
	public User completeTask(Task task) {
		var newXp = xp + task.reward();

		var newSubmissions = Stream.concat(this.submissions.stream(), Stream.of(new Submission(task, Instant.now())))
				.toList();

		return new User(this.telegramId, this.username, this.leetcodeURL, newXp, newSubmissions, this.lastCheckTime);
	}

	/**
	 * Обновить последний момент проверки.
	 *
	 * @return обновленный пользователь
	 */
	public User withLastCheckTime() {
		return new User(this.telegramId, this.username, this.leetcodeURL, this.xp, this.submissions, Instant.now());
	}

	/**
	 * Проверка отправлений за последние 24 часа.
	 */
	public void validateCheckRateLimit() {
		if (this.lastCheckTime.isAfter(Instant.now().minus(Duration.ofDays(1)))) {
			throw new TaskExceptions.TaskCheckRateLimitException(lastCheckTime);
		}
	}

	/**
	 * Получить решенные задачи пользователя за последнюю неделю.
	 *
	 * @return список завершенных задач за последнюю неделю
	 */
	public Set<Task> getCompletedTasksLastWeek() {
		Instant oneWeekAgo = Instant.now().minus(Duration.ofDays(7));

		return submissions.stream().filter(s -> s.completedAt().isAfter(oneWeekAgo)).map(Submission::task)
				.collect(Collectors.toSet());
	}
}

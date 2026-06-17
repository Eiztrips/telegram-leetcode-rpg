package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.SubmissionExceptions;
import lombok.Builder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Доменная модель пользователя
 *
 * @param telegramId
 *            уникальный индетификатор пользователя
 * @param leetcodeUsername
 *            имя пользователя на литкоде
 * @param xp
 *            опыт пользователя
 * @param lastCheckTime
 *            последнее время проверки сабмишенов
 * @param guildId
 *            id гильдии пользователя
 */
public record User(Long telegramId, String leetcodeUsername, int xp, Instant lastCheckTime, Long guildId) {

	@Builder
	public User {
		if (telegramId == null)
			throw new GlobalExceptions.ArgumentEmptyException("telegramId");
		if (leetcodeUsername == null || leetcodeUsername.isBlank())
			throw new GlobalExceptions.ArgumentEmptyException("leetcodeUsername");
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

		for (Submission submission : submissions)
			newXp += submission.getReward();

		return new User(this.telegramId, this.leetcodeUsername, newXp, this.lastCheckTime, this.guildId);
	}

	/**
	 * Обновить последний момент проверки.
	 *
	 * @return обновленный пользователь
	 */
	public User withLastCheckTime() {
		return new User(this.telegramId, this.leetcodeUsername, this.xp, Instant.now(), this.guildId);
	}

	/**
	 * Обновить гильдую пользователя.
	 *
	 * @param id
	 *            уникальный идентификатор пользователя
	 *
	 * @return обновленный пользователь
	 */
	public User withGuild(Long id) {
		return User.builder().telegramId(telegramId).leetcodeUsername(leetcodeUsername).xp(xp)
				.lastCheckTime(lastCheckTime).guildId(id).build();
	}

	/**
	 * Исключить пользователя из гильдии.
	 *
	 * @return обновленный пользователь
	 */
	public User withoutGuild() {
		return User.builder().telegramId(telegramId).leetcodeUsername(leetcodeUsername).xp(xp)
				.lastCheckTime(lastCheckTime).guildId(null).build();
	}

	/**
	 * Проверка отправлений за последние 24 часа.
	 */
	public void validateCheckRateLimit() {
		if (this.lastCheckTime != null && this.lastCheckTime.isAfter(Instant.now().minus(Duration.ofDays(1)))) {
			throw new SubmissionExceptions.SubmissionCheckRateLimitException(lastCheckTime);
		}
	}
}

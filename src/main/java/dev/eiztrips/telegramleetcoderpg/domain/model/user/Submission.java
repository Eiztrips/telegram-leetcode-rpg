package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.GlobalExceptions;
import lombok.Builder;

import java.time.Instant;

/**
 * Доменная модель сабмишена.
 *
 * @param taskSlug
 *            уникальное название задачи
 * @param taskDifficulty
 *            сложность задачи (ENUM)
 * @param completedAt
 *            время отправки сабмишена
 */
public record Submission(String taskSlug, Difficulty taskDifficulty, Instant completedAt) {

	@Builder
	public Submission {
		if (taskSlug == null)
			throw new GlobalExceptions.ArgumentEmptyException("task_slug");
		if (taskDifficulty == null)
			throw new GlobalExceptions.ArgumentEmptyException("taskDifficulty");
		if (completedAt == null)
			throw new GlobalExceptions.ArgumentEmptyException("completedAt");
	}

	/**
	 * Получить награду за задачу.
	 *
	 * @return награда
	 */
	public int getReward() {
		return taskDifficulty().getReward();
	}
}

package dev.eiztrips.telegramleetcoderpg.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;

import java.time.Instant;

/**
 * Модель выполненной задачи.
 */
public record Submission(String taskSlug, Difficulty taskDifficulty, Instant completedAt) {
	public Submission {
		if (taskSlug == null)
			throw new UserExceptions.ArgumentEmptyException("task_slug");
		if (taskDifficulty == null)
			throw new UserExceptions.ArgumentEmptyException("taskDifficulty");
		if (completedAt == null)
			throw new UserExceptions.ArgumentEmptyException("completedAt");
	}

	public int getReward() {
		return taskDifficulty().getReward();
	}
}

package dev.eiztrips.telegramleetcoderpg.core.domain.model.user;

import dev.eiztrips.telegramleetcoderpg.core.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.core.domain.model.task.Task;

import java.time.Instant;

/**
 * Модель выполненной задачи.
 */
public record Submission(Task task, Instant completedAt) {
	public Submission {
		if (task == null)
			throw new UserExceptions.ArgumentEmptyException("task");
		if (completedAt == null)
			throw new UserExceptions.ArgumentEmptyException("completedAt(" + task.title() + ")");
	}
}

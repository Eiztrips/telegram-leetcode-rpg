package dev.eiztrips.telegramleetcoderpg.core.domain.exception;

public sealed class DomainException extends RuntimeException permits TaskExceptions.TaskCheckRateLimitException,
		TaskExceptions.TaskNotFoundException, UserExceptions.ArgumentEmptyException,
		UserExceptions.InvalidLeetCodeUrlException, UserExceptions.UserAlreadyExistsException,
		UserExceptions.UserNotFoundException, WeeklyBossExceptions.InvalidDamageException,
		WeeklyBossExceptions.WeeklyBossAlreadyDefeated, WeeklyBossExceptions.WeeklyBossNotFoundException {
	protected DomainException(String message) {
		super(message);
	}
}

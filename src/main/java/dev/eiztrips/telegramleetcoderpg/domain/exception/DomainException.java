package dev.eiztrips.telegramleetcoderpg.domain.exception;

public sealed class DomainException extends RuntimeException permits GlobalExceptions.ArgumentEmptyException,
		GlobalExceptions.ArgumentInvalidException, SubmissionExceptions.SubmissionCheckRateLimitException,
		UserExceptions.UserAlreadyExistsException, UserExceptions.UserNotFoundException,
		WeeklyBossExceptions.WeeklyBossAlreadyDefeated, WeeklyBossExceptions.WeeklyBossNotFoundException {
	protected DomainException(String message) {
		super(message);
	}
}

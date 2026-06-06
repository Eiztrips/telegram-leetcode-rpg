package dev.eiztrips.telegramleetcoderpg.domain.exception;

public sealed class DomainException extends RuntimeException
		permits SubmissionExceptions.SubmissionCheckRateLimitException, UserExceptions.ArgumentEmptyException,
		UserExceptions.InvalidLeetCodeUrlException, UserExceptions.UserAlreadyExistsException,
		UserExceptions.UserNotFoundException, WeeklyBossExceptions.InvalidDamageException,
		WeeklyBossExceptions.WeeklyBossAlreadyDefeated, WeeklyBossExceptions.WeeklyBossNotFoundException {
	protected DomainException(String message) {
		super(message);
	}
}

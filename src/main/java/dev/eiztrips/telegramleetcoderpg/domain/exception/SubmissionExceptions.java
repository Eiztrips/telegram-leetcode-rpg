package dev.eiztrips.telegramleetcoderpg.domain.exception;

import java.time.*;

public final class SubmissionExceptions {
	private SubmissionExceptions() {
	}

	/**
	 * Исключение: рейт-лимит проверки отправок.
	 */
	public static final class SubmissionCheckRateLimitException extends DomainException {
		public SubmissionCheckRateLimitException(Instant lastSubmitted) {
			// Мувнуть кудато не сюда однажды
			Instant nextAvailableTime = lastSubmitted.plus(Duration.ofMinutes(30));
			Duration remaining = Duration.between(Instant.now(), nextAvailableTime);

			long minutes = remaining.toMinutesPart();

			super(String.format("Вы сможете использовать эту команду через %d мин.", minutes));
		}
	}
}

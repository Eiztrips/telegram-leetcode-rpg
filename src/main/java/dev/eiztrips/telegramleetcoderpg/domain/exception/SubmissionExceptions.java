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
			Instant nextAvailableTime = lastSubmitted.plus(Duration.ofDays(1));
			Duration remaining = Duration.between(Instant.now(), nextAvailableTime);

			long hours = remaining.toHours();
			long minutes = remaining.toMinutesPart();

			super(String.format("Вы сможете использовать эту команду через %d ч. %d мин.", hours, minutes));
		}
	}
}

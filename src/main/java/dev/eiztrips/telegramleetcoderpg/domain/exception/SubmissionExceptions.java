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

			super("⏳ <b>Команда временно недоступна!</b>\n\nПовторите через <b>%d</b> мин.".formatted(minutes));
		}
	}
}

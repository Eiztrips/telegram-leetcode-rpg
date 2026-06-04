package dev.eiztrips.telegramleetcoderpg.core.domain.exception;

import java.time.*;

public final class TaskExceptions {
	private TaskExceptions() {
	}

	/**
	 * Исключение: рейт-лимит проверки отправок.
	 */
	public static final class TaskCheckRateLimitException extends DomainException {
		public TaskCheckRateLimitException(Instant lastSubmitted) {
			// Мувнуть кудато не сюда однажды
			Instant nextAvailableTime = lastSubmitted.plus(Duration.ofDays(1));
			Duration remaining = Duration.between(Instant.now(), nextAvailableTime);

			long hours = remaining.toHours();
			long minutes = remaining.toMinutesPart();

			super(String.format("Вы сможете использовать эту команду через %d ч. %d мин.", hours, minutes));
		}
	}

	/**
	 * Исключение: задача не найдена
	 */
	public static final class TaskNotFoundException extends DomainException {
		public TaskNotFoundException(Long id) {
			super("Задача " + id + " не найдена");
		}
	}
}

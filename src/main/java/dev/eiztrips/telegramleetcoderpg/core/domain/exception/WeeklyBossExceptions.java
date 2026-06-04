package dev.eiztrips.telegramleetcoderpg.core.domain.exception;

public final class WeeklyBossExceptions {
	private WeeklyBossExceptions() {
	}

	/**
	 * Исключение: босс не найден.
	 */
	public static final class WeeklyBossNotFoundException extends DomainException {
		public WeeklyBossNotFoundException(Long id) {
			super("Босс " + id + " не найден");
		}
	}

	/**
	 * Исключение: босс уже повержен.
	 */
	public static final class WeeklyBossAlreadyDefeated extends DomainException {
		public WeeklyBossAlreadyDefeated(Long id) {
			super("Босс " + id + " уже повержен");
		}
	}

	/**
	 * Исключение: урон не может быть отрицательным
	 */
	public static final class InvalidDamageException extends DomainException {
		public InvalidDamageException() {
			super("Урон не может быть отрицательным");
		}
	}
}

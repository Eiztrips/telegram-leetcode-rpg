package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class WeeklyBossExceptions {
	private WeeklyBossExceptions() {
	}

	/**
	 * Исключение: босс не найден.
	 */
	public static final class WeeklyBossNotFoundException extends DomainException {
		public WeeklyBossNotFoundException(Long id) {
			super("❌ Босс <b>%d</b> не найден".formatted(id));
		}
	}

	/**
	 * Исключение: босс уже повержен.
	 */
	public static final class WeeklyBossAlreadyDefeated extends DomainException {
		public WeeklyBossAlreadyDefeated(Long id) {
			super("🏆 Босс <b>%d</b> уже повержен!".formatted(id));
		}
	}
}

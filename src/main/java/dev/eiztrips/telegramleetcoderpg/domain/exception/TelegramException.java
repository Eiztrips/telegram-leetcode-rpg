package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class TelegramException {
	private TelegramException() {

	}

	/**
	 * Исключение: неизвестная команда
	 */
	public static final class InvalidCommandException extends DomainException {
		public InvalidCommandException(String message) {
			super("Не верный формат команды! Пример: " + message);
		}
	}
}

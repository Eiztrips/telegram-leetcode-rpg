package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class GlobalExceptions {
	private GlobalExceptions() {
	}

	/**
	 * Исключение: пустое поле.
	 */
	public static final class ArgumentEmptyException extends DomainException {
		public ArgumentEmptyException(String arg) {
			super("⚠️ <b>%s</b> не может быть пустым.".formatted(arg));
		}
	}

	/**
	 * Исключение: некорректное поле.
	 */
	public static final class ArgumentInvalidException extends DomainException {
		public ArgumentInvalidException(String message) {
			super("❌ <b>Ошибка:</b> %s".formatted(message));
		}
	}
}

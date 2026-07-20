package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class TelegramExceptions {
	private TelegramExceptions() {

	}

	/**
	 * Исключение: неизвестная команда
	 */
	public static final class InvalidCommandException extends DomainException {
		public InvalidCommandException(String message) {
			super("⚠️ <b>Неверный формат команды!</b>\n\nПример: <code>%s</code>".formatted(message));
		}
	}

	public static final class ToManyRequestException extends DomainException {
		public ToManyRequestException() {
			super("⏳ <b>Слишком много команд!</b>\n\nДождитесь обработки предыдущих команд.");
		}
	}
}

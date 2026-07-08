package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class TelegramExceptions {
	private TelegramExceptions() {

	}

	/**
	 * Исключение: неизвестная команда
	 */
	public static final class InvalidCommandException extends DomainException {
		public InvalidCommandException(String message) {
			super("Не верный формат команды! Пример: " + message);
		}
	}

	public static final class ToManyRequestException extends DomainException {
		public ToManyRequestException() {
			super("Вы отправили слишком много команд, дождитесь их обработки!");
		}
	}
}

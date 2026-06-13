package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class ClientExceptions {
	private ClientExceptions() {

	}

	/**
	 * Исключение: чат не найден.
	 */
	public static final class ChatNotFoundException extends DomainException {
		public ChatNotFoundException(Long id) {
			super(String.format("Чат с id: %d, не найден!", id));
		}
	}
}

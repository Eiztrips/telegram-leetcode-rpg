package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class UserExceptions {
	private UserExceptions() {
	}

	/**
	 * Исключение: пользователь не найден.
	 */
	public static final class UserNotFoundException extends DomainException {
		public UserNotFoundException(Long id) {
			super("Пользователь " + id + " не найден");
		}
	}

	/**
	 * Исключение: пользователь уже существует.
	 */
	public static final class UserAlreadyExistsException extends DomainException {
		public UserAlreadyExistsException() {
			super("Пользователь уже зарегистрирован");
		}
	}
}

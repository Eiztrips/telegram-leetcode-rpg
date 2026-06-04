package dev.eiztrips.telegramleetcoderpg.core.domain.exception;

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

	/**
	 * Исключение: пустое поле.
	 */
	public static final class ArgumentEmptyException extends DomainException {
		public ArgumentEmptyException(String arg) {
			super(arg + " не может быть пустым.");
		}
	}

	/**
	 * Исключение: некорректный формат leetcode ссылки.
	 */
	public static final class InvalidLeetCodeUrlException extends DomainException {
		public InvalidLeetCodeUrlException(String url) {
			super(String.format(
					"Некорректный формат ссылки: '%s'. Пример правильной ссылки: https://leetcode.com/u/<name>", url));
		}
	}
}

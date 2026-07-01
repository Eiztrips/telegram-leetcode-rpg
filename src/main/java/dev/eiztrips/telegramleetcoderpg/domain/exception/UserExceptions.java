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
		public UserNotFoundException() {
			super("Вы не зарегистрированы, напишите /start мне в лс!");
		}
	}

	/**
	 * Исключение: пользователь не состоит в гильдии.
	 */
	public static final class UserGuildNotFoundException extends DomainException {
		public UserGuildNotFoundException(Long id) {
			super("Пользователь + " + id + ", не состоит в гильдии");
		}
		public UserGuildNotFoundException() {
			super("Вы не состоите в гильдии");
		}
	}

	/**
	 * Исключение: пользователь с таким id уже зарегестрирован.
	 */
	public static final class UserAlreadyExistsException extends DomainException {
		public UserAlreadyExistsException() {
			super("Вы уже зарегистрированы");
		}
	}

	/**
	 * Исключение: пользователь с таким юзернеймом уже зарегестрирован.
	 */
	public static final class LeetcodeUsernameAlreadyExistsException extends DomainException {
		public LeetcodeUsernameAlreadyExistsException() {
			super("Пользователь с таким юзернеймом уже зарегистрирован");
		}
	}

	/**
	 * Исключение: токен регистрации истек или не найден.
	 */
	public static final class RegistrationTokenExpiredException extends DomainException {
		public RegistrationTokenExpiredException() {
			super("Ваш токен регистрации не был найден, повторите попытку регистрации");
		}
	}

	/**
	 * Исключение: токен не совпал с текущим.
	 */
	public static final class BadRegistrationTokenException extends DomainException {
		public BadRegistrationTokenException() {
			super("В описании профиля не найден подходящий токен");
		}
	}
}

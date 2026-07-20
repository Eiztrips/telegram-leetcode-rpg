package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class UserExceptions {
	private UserExceptions() {
	}

	/**
	 * Исключение: пользователь не найден.
	 */
	public static final class UserNotFoundException extends DomainException {
		public UserNotFoundException(Long id) {
			super("❌ Пользователь <b>%d</b> не найден".formatted(id));
		}
		public UserNotFoundException() {
			super("⚠️ <b>Вы не зарегистрированы!</b>\n\nНапишите /start мне в личные сообщения.");
		}
	}

	/**
	 * Исключение: пользователь не состоит в гильдии.
	 */
	public static final class UserGuildNotFoundException extends DomainException {
		public UserGuildNotFoundException(Long id) {
			super("❌ Пользователь <b>%d</b> не состоит в гильдии".formatted(id));
		}
		public UserGuildNotFoundException() {
			super("⚠️ <b>Вы не состоите в гильдии!</b>\n\nВступите в гильдию командой /join.");
		}
	}

	/**
	 * Исключение: пользователь с таким id уже зарегестрирован.
	 */
	public static final class UserAlreadyExistsException extends DomainException {
		public UserAlreadyExistsException() {
			super("⚠️ <b>Вы уже зарегистрированы!</b>");
		}
	}

	/**
	 * Исключение: пользователь с таким юзернеймом уже зарегестрирован.
	 */
	public static final class LeetcodeUsernameAlreadyExistsException extends DomainException {
		public LeetcodeUsernameAlreadyExistsException() {
			super("⚠️ <b>Пользователь с таким юзернеймом уже зарегистрирован</b>");
		}
	}

	/**
	 * Исключение: токен регистрации истек или не найден.
	 */
	public static final class RegistrationTokenExpiredException extends DomainException {
		public RegistrationTokenExpiredException() {
			super("⏰ <b>Токен не найден!</b>\n\nПовторите регистрацию командой /register.");
		}
	}

	/**
	 * Исключение: токен не совпал с текущим.
	 */
	public static final class BadRegistrationTokenException extends DomainException {
		public BadRegistrationTokenException() {
			super("❌ <b>Токен не найден в профиле!</b>\n\nПроверьте описание профиля и попробуйте /verify снова.");
		}
	}
}

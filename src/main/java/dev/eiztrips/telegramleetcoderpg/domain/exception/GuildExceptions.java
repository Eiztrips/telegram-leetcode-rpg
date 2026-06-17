package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class GuildExceptions {
	private GuildExceptions() {
	}

	/**
	 * Исключение: гильдия не найдена.
	 */
	public static final class GuildNotFoundException extends DomainException {
		public GuildNotFoundException(Long id) {
			super(String.format("Гильдия с id: %d - не найдена", id));
		}
	}

	/**
	 * Исключение: гильдия уже существует.
	 */
	public static final class GuildAlreadyExists extends DomainException {
		public GuildAlreadyExists(Long id) {
			super(String.format("Гильдия с id: %d - уже существует", id));
		}
	}

	/**
	 * Исключение: пользователь уже в гильдии.
	 */
	public static final class UserAlreadyExistsInGuild extends DomainException {
		public UserAlreadyExistsInGuild(Long id) {
			super(String.format("Пользователь: %d - уже состоит в гильдии", id));
		}
	}

	/**
	 * Исключение: пользователь не найден в гильдии.
	 */
	public static final class UserNotFoundInGuild extends DomainException {
		public UserNotFoundInGuild(Long id) {
			super(String.format("Пользователь: %d - не состоит в гильдии", id));
		}
	}

	/**
	 * Исключение: босс гильдии не существует.
	 */
	public static final class GuildBossNotFountException extends DomainException {
		public GuildBossNotFountException() {
			super("Босс гильдии не существует");
		}
	}
}

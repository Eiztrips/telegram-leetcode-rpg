package dev.eiztrips.telegramleetcoderpg.domain.exception;

public final class GuildExceptions {
	private GuildExceptions() {
	}

	/**
	 * Исключение: гильдия не найдена.
	 */
	public static final class GuildNotFoundException extends DomainException {
		public GuildNotFoundException(Long id) {
			super("❌ Гильдия <b>%d</b> не найдена".formatted(id));
		}
	}

	/**
	 * Исключение: гильдия уже существует.
	 */
	public static final class GuildAlreadyExists extends DomainException {
		public GuildAlreadyExists(Long id) {
			super("⚠️ Гильдия <b>%d</b> уже существует".formatted(id));
		}
	}

	/**
	 * Исключение: пользователь уже в гильдии.
	 */
	public static final class UserAlreadyExistsInGuild extends DomainException {
		public UserAlreadyExistsInGuild(Long id) {
			super("⚠️ Пользователь <b>%d</b> уже состоит в гильдии".formatted(id));
		}
	}

	/**
	 * Исключение: пользователь не найден в гильдии.
	 */
	public static final class UserNotFoundInGuild extends DomainException {
		public UserNotFoundInGuild(Long id) {
			super("❌ Пользователь <b>%d</b> не состоит в гильдии".formatted(id));
		}
	}

	/**
	 * Исключение: босс гильдии не существует.
	 */
	public static final class GuildBossNotFoundException extends DomainException {
		public GuildBossNotFoundException() {
			super("💀 <b>Босс гильдии не существует</b>");
		}
	}
}

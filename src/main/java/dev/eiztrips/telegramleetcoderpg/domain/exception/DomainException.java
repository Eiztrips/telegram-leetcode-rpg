package dev.eiztrips.telegramleetcoderpg.domain.exception;

public sealed class DomainException extends RuntimeException
		permits ClientExceptions.ChatNotFoundException, GlobalExceptions.ArgumentEmptyException,
		GlobalExceptions.ArgumentInvalidException, GuildExceptions.GuildAlreadyExists,
		GuildExceptions.GuildBossNotFountException, GuildExceptions.GuildNotFoundException,
		GuildExceptions.UserAlreadyExistsInGuild, GuildExceptions.UserNotFoundInGuild,
		SubmissionExceptions.SubmissionCheckRateLimitException, TelegramException.InvalidCommandException,
		UserExceptions.LeetcodeUsernameAlreadyExistsException, UserExceptions.TelegramIdAlreadyExistsException,
		UserExceptions.UserGuildNotFoundException, UserExceptions.UserNotFoundException,
		WeeklyBossExceptions.WeeklyBossAlreadyDefeated, WeeklyBossExceptions.WeeklyBossNotFoundException {
	protected DomainException(String message) {
		super(message);
	}
}

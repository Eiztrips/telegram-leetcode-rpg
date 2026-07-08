package dev.eiztrips.telegramleetcoderpg.domain.exception;

public sealed class DomainException extends RuntimeException permits ClientExceptions.ChatNotFoundException,
		GlobalExceptions.ArgumentEmptyException, GlobalExceptions.ArgumentInvalidException,
		GuildExceptions.GuildAlreadyExists, GuildExceptions.GuildBossNotFoundException,
		GuildExceptions.GuildNotFoundException, GuildExceptions.UserAlreadyExistsInGuild,
		GuildExceptions.UserNotFoundInGuild, SubmissionExceptions.SubmissionCheckRateLimitException,
		TelegramExceptions.InvalidCommandException, TelegramExceptions.ToManyRequestException,
		UserExceptions.BadRegistrationTokenException, UserExceptions.LeetcodeUsernameAlreadyExistsException,
		UserExceptions.RegistrationTokenExpiredException, UserExceptions.UserAlreadyExistsException,
		UserExceptions.UserGuildNotFoundException, UserExceptions.UserNotFoundException,
		WeeklyBossExceptions.WeeklyBossAlreadyDefeated, WeeklyBossExceptions.WeeklyBossNotFoundException {
	protected DomainException(String message) {
		super(message);
	}
}

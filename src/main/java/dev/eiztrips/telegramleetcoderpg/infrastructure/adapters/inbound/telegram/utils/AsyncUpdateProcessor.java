package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils;

import dev.eiztrips.telegramleetcoderpg.domain.exception.*;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class AsyncUpdateProcessor {
	private final List<CommandHandler> commandHandlers;

	@Async
	public void process(Update update, Set<Long> lockedUsers, Consumer<String> responseConsumer,
			Runnable unknowCommandRunnable) {
		Long userId = update.getMessage().getChatId();

		try {
			String text = update.getMessage().getText();

			CommandHandler handler = commandHandlers.stream().filter(h -> h.canHandle(text)).findFirst().orElse(null);

			if (handler == null) {
				unknowCommandRunnable.run();
				return;
			}

			String responseText;

			try {
				responseText = handler.handle(update);
			} catch (DomainException e) {
				responseText = resolveDomainException(e);
			} catch (Exception e) {
				responseText = "Произошла непредвиденная ошибка: " + e.getMessage();
			}

			responseConsumer.accept(responseText);
		} finally {
			lockedUsers.remove(userId);
		}
	}

	private String resolveDomainException(DomainException e) {
		return switch (e) {
			case WeeklyBossExceptions.WeeklyBossNotFoundException wbnfe -> wbnfe.getMessage();
			case WeeklyBossExceptions.WeeklyBossAlreadyDefeated wbad -> wbad.getMessage();
			case UserExceptions.UserNotFoundException unfe -> unfe.getMessage();
			case UserExceptions.UserAlreadyExistsException uaee -> uaee.getMessage();
			case TelegramException.InvalidCommandException ice -> ice.getMessage();
			case SubmissionExceptions.SubmissionCheckRateLimitException scrle -> scrle.getMessage();
			case GlobalExceptions.ArgumentEmptyException aee -> aee.getMessage(); // аеееее
			case GlobalExceptions.ArgumentInvalidException aie -> aie.getMessage();
			default -> "Незвестная ошибка бизнесс-логики";
		};
	}
}

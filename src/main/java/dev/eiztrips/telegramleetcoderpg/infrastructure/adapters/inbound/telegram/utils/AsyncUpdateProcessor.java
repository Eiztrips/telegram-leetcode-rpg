package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.*;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.RegisterHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.StartHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.VerificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncUpdateProcessor {
	private final List<CommandHandler> commandHandlers;
	private final UserRepositoryPort userRepositoryPort;
	private final RegisterHandler registerHandler;
	private final VerificationHandler verificationHandler;

	@Async
	public void process(Update update, Set<Long> lockedUsers, Consumer<String> responseConsumer,
			Runnable helloCommandRunnable) {
		Long userId = update.getMessage().getChatId();

		try {
			CommandHandler handler = commandHandlers.stream().filter(h -> h.canHandle(update)).findFirst().orElse(null);

			if (handler == null || isStart(handler, helloCommandRunnable))
				return;

			String responseText;

			try {
				checkUserRegistration(update);
				responseText = handler.handle(update);
			} catch (DomainException e) {
				responseText = resolveDomainException(e);
			} catch (Exception e) {
				responseText = "Произошла непредвиденная ошибка";
				log.error(e.getMessage());
			}

			responseConsumer.accept(responseText);
		} finally {
			lockedUsers.remove(userId);
		}
	}

	private String resolveDomainException(DomainException e) {
		return e.getMessage();
	}

	private void checkUserRegistration(Update update) {
		if (!update.getMessage().hasText())
			return;
		String text = update.getMessage().getText();
		if (text.startsWith(registerHandler.getCommand()) || text.startsWith(verificationHandler.getCommand()))
			return;

		userRepositoryPort.getByTelegramId(update.getMessage().getFrom().getId())
				.orElseThrow(UserExceptions.UserNotFoundException::new);
	}

	private boolean isStart(CommandHandler handler, Runnable helloCommandRunnable) {
		if (handler.getClass().equals(StartHandler.class)) {
			helloCommandRunnable.run();
			return true;
		}
		return false;
	}
}

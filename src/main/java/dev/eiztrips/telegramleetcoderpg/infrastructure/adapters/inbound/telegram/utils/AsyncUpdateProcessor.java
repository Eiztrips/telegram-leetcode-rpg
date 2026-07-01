package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.*;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.RegisterHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.StartHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.VerificationHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.LongConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncUpdateProcessor {
	private final List<CommandHandler> commandHandlers;
	private final UserRepositoryPort userRepositoryPort;
	private final RegisterHandler registerHandler;
	private final VerificationHandler verificationHandler;
	private final ObjectProvider<AsyncUpdateProcessor> selfProvider;

	@Async
	public void process(Long userId, Map<Long, Deque<Update>> updateUserQueueMap, Set<Long> lockedUsers,
			BiConsumer<String, Long> responseConsumer, LongConsumer helloCommandConsumer) {
		var updateUserQueue = updateUserQueueMap.get(userId);

		if (updateUserQueue == null || updateUserQueue.isEmpty())
			return;

		try {
			while (!updateUserQueue.isEmpty()) {
				var update = updateUserQueue.pollFirst();
				if (update == null)
					continue;
				CommandHandler handler = commandHandlers.stream().filter(h -> h.canHandle(update)).findFirst()
						.orElse(null);

				if (handler == null || isStart(handler, helloCommandConsumer, update.getMessage().getChatId()))
					continue;

				String responseText = executeHandler(handler, update);

				responseConsumer.accept(responseText, update.getMessage().getChatId());
			}
		} finally {
			lockedUsers.remove(userId);

			if (!updateUserQueue.isEmpty()) {
				if (lockedUsers.add(userId)) {
					AsyncUpdateProcessor self = selfProvider.getIfAvailable();
					if (self != null)
						self.process(userId, updateUserQueueMap, lockedUsers, responseConsumer, helloCommandConsumer);
				}
			} else {
				updateUserQueueMap.remove(userId);
			}
		}
	}

	private String executeHandler(CommandHandler handler, Update update) {
		try {
			checkUserRegistration(update);
			return handler.handle(update);
		} catch (DomainException e) {
			return resolveDomainException(e);
		} catch (Exception e) {
			log.error(e.getMessage());
			return "Произошла непредвиденная ошибка";
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

	private boolean isStart(CommandHandler handler, LongConsumer helloCommandRunnable, Long chatId) {
		if (handler.getClass().equals(StartHandler.class)) {
			helloCommandRunnable.accept(chatId);
			return true;
		}
		return false;
	}
}

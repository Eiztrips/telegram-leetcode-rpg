package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.*;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.registration.RegisterHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.StartHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.registration.VerificationHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
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
	private final EntityManager entityManager;

	@Async
	public void process(Long userId, Map<Long, Deque<Update>> updateUserQueueMap, Set<Long> lockedUsers,
			BiConsumer<String, Long> responseConsumer, LongConsumer helloCommandConsumer) {
		var updateUserQueue = updateUserQueueMap.get(userId);

		if (updateUserQueue == null || updateUserQueue.isEmpty())
			return;

		try {
			while (!updateUserQueue.isEmpty()) {
				var update = updateUserQueue.pollFirst();

				CommandHandler handler = commandHandlers.stream().filter(h -> h.canHandle(update)).findFirst()
						.orElse(null);

				if (handler == null || isStart(handler, helloCommandConsumer, update.getMessage().getChatId()))
					continue;

				log.info("Начало обработки комманды: {} (user: {})", handler.getCommand(), userId);

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
		int maxAttempts = 3;

		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				checkUserRegistration(update);
				return handler.handle(update);
			} catch (DomainException e) {
				return resolveDomainException(e);
			} catch (OptimisticLockException _) {
				if (attempt == maxAttempts) {
					log.warn("OptimisticLockException: превышено количество попыток ({})", maxAttempts);
					return "Сервер перегружен, повторите попытку позже";
				}
				log.warn("OptimisticLockException: повтор {} из {}", attempt + 1, maxAttempts);
				entityManager.clear();
				try {
					Thread.sleep(100L * (1L << (attempt - 1)));
				} catch (InterruptedException _) {
					Thread.currentThread().interrupt();
					return "Обработка команды была прервана, повторите попытку";
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				return "Произошла непредвиденная ошибка";
			}
		}
		return "Сервер перегружен, повторите попытку позже";
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

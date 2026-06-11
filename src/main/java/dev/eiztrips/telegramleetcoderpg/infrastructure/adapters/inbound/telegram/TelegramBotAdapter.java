package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils.UpdateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TelegramBotAdapter extends TelegramLongPollingBot {
	private final Set<Long> lockedUsers = ConcurrentHashMap.newKeySet();

	private final String botUsername;
	private final List<CommandHandler> commandHandlers;
	private final UpdateProcessor updateProcessor;

	public TelegramBotAdapter(@Value("${telegram.bot.username}") String botUsername,
			@Value("${telegram.bot.token}") String botToken, List<CommandHandler> commandHandlers,
			UpdateProcessor updateProcessor) {
		super(botToken);
		this.botUsername = botUsername;
		this.commandHandlers = commandHandlers;
		this.updateProcessor = updateProcessor;
	}

	@Override
	public String getBotUsername() {
		return this.botUsername;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update == null || !update.hasMessage() || !update.getMessage().hasText())
			return;

		Long userId = update.getMessage().getChatId();

		if (!lockedUsers.add(userId)) {
			executeMessage(SendMessage.builder().chatId(userId).text("Не спамь").build());
			return;
		}

		updateProcessor.process(update, lockedUsers,
				responseText -> executeMessage(SendMessage.builder().chatId(userId).text(responseText).build()),
				() -> sendUnknownCommandMessage(userId));
	}

	private void sendUnknownCommandMessage(long chatId) {
		StringBuilder commands = new StringBuilder("Неизвестная команда. Доступные команды:\n");
		for (CommandHandler h : this.commandHandlers) {
			commands.append(h.getCommandExample()).append('\n');
		}
		executeMessage(new SendMessage(String.valueOf(chatId), commands.toString()));
	}

	private void executeMessage(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			log.error("Ошибка отправки сообщения в Telegram: {}", e.getMessage());
		}
	}
}

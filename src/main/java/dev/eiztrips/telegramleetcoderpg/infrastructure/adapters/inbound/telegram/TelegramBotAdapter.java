package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.ClientPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.utils.AsyncUpdateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class TelegramBotAdapter extends TelegramLongPollingBot implements ClientPort {
	private final Set<Long> lockedUsers = ConcurrentHashMap.newKeySet();

	private final String botUsername;
	private final List<CommandHandler> commandHandlers;
	private final AsyncUpdateProcessor asyncUpdateProcessor;

	public TelegramBotAdapter(@Value("${telegram.bot.username}") String botUsername,
			@Value("${telegram.bot.token}") String botToken, List<CommandHandler> commandHandlers,
			AsyncUpdateProcessor asyncUpdateProcessor) {
		super(botToken);
		this.botUsername = botUsername;
		this.commandHandlers = commandHandlers;
		this.asyncUpdateProcessor = asyncUpdateProcessor;
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

		asyncUpdateProcessor.process(update, lockedUsers,
				responseText -> executeMessage(SendMessage.builder().chatId(userId).text(responseText).build()),
				() -> sendHelloCommandMessage(userId));
	}

	private void sendHelloCommandMessage(long chatId) {
		var hello = "Приветствую в новом RPG мире leetcode приключений! \nВот список существующих команд:\n";

		StringBuilder commands = new StringBuilder("<blockquote>" + hello + "</blockquote>\n \n");

		for (CommandHandler h : this.commandHandlers) {
			String example = h.getCommandExample();
			String description = h.getCommandDescription();

			commands.append("<b> ").append(example).append("</b>\n").append("<i> • ").append(description)
					.append("</i>\n\n");
		}

		var message = new SendMessage(String.valueOf(chatId), commands.toString());

		executeMessage(message);
	}

	private void executeMessage(SendMessage message) {
		try {
			message.setParseMode("html");
			execute(message);
		} catch (TelegramApiException e) {
			log.error("Ошибка отправки сообщения в Telegram: {}", e.getMessage());
		}
	}

	@Override
	public boolean chatExists(Long chatId) {
		try {
			GetChat getChat = GetChat.builder().chatId(chatId).build();
			execute(getChat);
			return true;
		} catch (TelegramApiException _) {
			return false;
		}
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram;

import dev.eiztrips.telegramleetcoderpg.domain.exception.*;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramBotAdapter extends TelegramLongPollingBot {
	private final String botUsername;
	private final List<CommandHandler> commandHandlers;

	public TelegramBotAdapter(@Value("${telegram.bot.username}") String botUsername,
			@Value("${telegram.bot.token}") String botToken, List<CommandHandler> commandHandlers) {
		super(botToken);
		this.botUsername = botUsername;
		this.commandHandlers = commandHandlers;
	}

	@Override
	public String getBotUsername() {
		return this.botUsername;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update == null || !update.hasMessage() || !update.getMessage().hasText())
			return;

		String text = update.getMessage().getText();
		long chatId = update.getMessage().getChatId();

		CommandHandler handler = commandHandlers.stream().filter(h -> h.canHandle(text)).findFirst().orElse(null);

		if (handler == null) {
			sendUnknownCommandMessage(chatId);
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

		executeMessage(SendMessage.builder().chatId(chatId).text(responseText).build());
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
			System.err.println("Ошибка отправки сообщения в Telegram: " + e.getMessage());
		}
	}
}

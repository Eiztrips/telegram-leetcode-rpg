package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.AddUserToGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class JoinToGuildHandler extends GroupChatHandler implements CommandHandler {

	private final AddUserToGuildUseCase addUserToGuildUseCase;

	@Override
	public boolean canHandle(Update update) {
		var text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	@Transactional
	public String handle(Update update) {
		var userId = update.getMessage().getFrom().getId();
		var guildId = update.getMessage().getChatId();

		addUserToGuildUseCase.addUserToGuild(userId, guildId);

		return "Вы были успешно добавлены";
	}

	@Override
	public String getCommand() {
		return "/join";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}
}

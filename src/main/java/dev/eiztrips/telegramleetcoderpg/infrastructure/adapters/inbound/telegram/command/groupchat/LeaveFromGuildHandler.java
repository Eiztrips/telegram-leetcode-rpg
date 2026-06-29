package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.RemoveUserFromGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(4)
public class LeaveFromGuildHandler extends GroupChatHandler implements CommandHandler {

	private final RemoveUserFromGuildUseCase removeUserFromGuildUseCase;

	@Override
	public boolean canHandle(Update update) {
		var text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	@Transactional
	public String handle(Update update) {
		var userId = update.getMessage().getFrom().getId();
		var groupId = update.getMessage().getChatId();

		removeUserFromGuildUseCase.removeUserFromGuild(userId, groupId);

		return "Вы успешно покинули группу";
	}

	@Override
	public String getCommand() {
		return "/leave";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return "Покинуть гильдию. Доступно только в общих чатах," + " при условии что вы находитесь в этой гильдии"
				+ " и сама гильдия существет.";
	}
}

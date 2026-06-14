package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class GroupChatHandler {
	protected boolean canHandle(Update update) {
		return update.getMessage().getChat().getType().equals("group");
	}
}

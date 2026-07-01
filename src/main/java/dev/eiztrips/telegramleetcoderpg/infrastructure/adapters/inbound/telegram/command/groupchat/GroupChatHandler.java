package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class GroupChatHandler {
	protected boolean canHandle(Update update) {
		String type = update.getMessage().getChat().getType();
		return type.equals("group") || type.equals("supergroup");
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat;

import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class PrivateChatHandler {
	protected boolean canHandle(Update update) {
		return update.getMessage().getChat().getType().equals("private");
	}
}

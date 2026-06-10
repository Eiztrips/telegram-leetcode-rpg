package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {

	boolean canHandle(String text);

	String handle(Update update);

	String getCommand();

	String getCommandExample();
}

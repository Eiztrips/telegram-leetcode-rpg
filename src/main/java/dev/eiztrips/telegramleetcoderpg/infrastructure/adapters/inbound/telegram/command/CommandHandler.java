package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public interface CommandHandler {

	boolean canHandle(Update update);

	default boolean isAlias(String message) {
		var parts = message.split(" ");
		return parts.length > 0 && getCommandAliases().contains(parts[0]);
	}

	String handle(Update update);

	String getCommand();

	default Set<String> getCommandAliases() {
		return Set.of(getCommand());
	}

	String getCommandExample();

	String getCommandDescription();
}

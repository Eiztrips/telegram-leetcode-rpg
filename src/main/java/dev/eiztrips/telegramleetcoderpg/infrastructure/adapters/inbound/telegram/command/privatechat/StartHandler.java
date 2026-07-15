package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Component
@Order(1)
public class StartHandler extends PrivateChatHandler implements CommandHandler {

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && isAlias(text);
	}

	@Override
	public String handle(Update update) {
		return "";
	}

	@Override
	public String getCommand() {
		return "/start";
	}

	@Override
	public Set<String> getCommandAliases() {
		return Set.of(getCommand(), "/info", "/help");
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return "Узнать существующие команды.";
	}
}

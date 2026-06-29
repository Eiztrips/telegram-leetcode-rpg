package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(1)
public class StartHandler extends PrivateChatHandler implements CommandHandler {

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
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
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return "Узнать существующие команды.";
	}
}

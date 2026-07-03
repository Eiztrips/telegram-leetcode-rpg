package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.GetOrCreateGuildInfoUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.dto.GuildInfoResult;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramGuildMessagePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(4)
public class GuildInfoHandler extends GroupChatHandler implements CommandHandler {

	private final GetOrCreateGuildInfoUseCase useCase;

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		Long chatId = update.getMessage().getChatId();
		Long fromId = update.getMessage().getFrom().getId();
		String title = update.getMessage().getChat().getTitle();

		GuildInfoResult gr = useCase.getOrCreateGuild(chatId, fromId);

		return TelegramGuildMessagePresenter.formatGuildInfo(title, gr.isCreated(), gr.currentBoss(), gr.users());
	}

	@Override
	public String getCommand() {
		return "/guild";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return "Получить информацию или создать гильдию." + " Доступно только в общих чатах.";
	}
}

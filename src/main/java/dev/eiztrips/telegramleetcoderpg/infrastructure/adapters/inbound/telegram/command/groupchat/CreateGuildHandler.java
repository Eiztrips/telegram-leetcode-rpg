package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.CreateGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CreateGuildHandler extends GroupChatHandler implements CommandHandler {

	private final CreateGuildUseCase createGuildUseCase;
	private final UserRepositoryPort userRepositoryPort;

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	@Transactional
	public String handle(Update update) {
		Long chatId = update.getMessage().getChatId();

		userRepositoryPort.getByTelegramId(update.getMessage().getFrom().getId())
				.orElseThrow(UserExceptions.UserNotFoundException::new);

		createGuildUseCase.create(chatId);

		return "Гильдия была успешно создана";
	}

	@Override
	public String getCommand() {
		return "/create-guild";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(3)
public class VerificationHandler extends PrivateChatHandler implements CommandHandler {

	private final RegisterHandler registerHandler;
	private final RegisterUserUseCase registerUserUseCase;

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		var userId = update.getMessage().getFrom().getId();
		var user = registerUserUseCase.completeUserRegistration(userId);

		if (user != null)
			return "<b>Успешная регистрация!</b>";

		return "<b>Что-то пошло не так...</b>";
	}

	@Override
	public String getCommand() {
		return "/verify";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return String.format("Подтвердить регистрацию (после %s!). Использовать только в личном чате с ботом.",
				registerHandler.getCommand());
	}
}

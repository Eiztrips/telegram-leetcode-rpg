package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.registration;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.PrivateChatHandler;
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
		return super.canHandle(update) && isAlias(text);
	}

	@Override
	public String handle(Update update) {
		var userId = update.getMessage().getFrom().getId();
		var user = registerUserUseCase.completeUserRegistration(userId);

		if (user != null)
			return "✅ <b>Регистрация завершена!</b>\n\nДобро пожаловать в мир RPG LeetCode!\nИспользуйте /profile чтобы посмотреть свой профиль.";

		return "❌ <b>Ошибка регистрации</b>\n\nПроверьте что токен вставлен в описание профиля, и попробуйте /verify снова.";
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
		return String.format("Подтвердить регистрацию (после %s!).", registerHandler.getCommand());
	}
}

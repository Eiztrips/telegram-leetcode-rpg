package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.domain.exception.TelegramException;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(2)
public class RegisterHandler extends PrivateChatHandler implements CommandHandler {
	private final RegisterUserUseCase registerUserUseCase;

	public RegisterHandler(RegisterUserUseCase registerUserUseCase) {
		this.registerUserUseCase = registerUserUseCase;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		String[] parts = update.getMessage().getText().trim().split("\\s+");

		if (parts.length != 2)
			throw new TelegramException.InvalidCommandException(getCommandExample());

		Long userId = update.getMessage().getFrom().getId();
		String leetcodeUsername = parts[1];

		String token = registerUserUseCase.startUserRegistration(userId, leetcodeUsername);

		return String.format(
				"<b>Ваш токен регистрации:</b> %n<code>%s</code> %n%n<b>Введите его в описание своего профиля на leetcode (readme).</b>%n%s",
				token, "https://leetcode.com/settings/profile");
	}

	@Override
	public String getCommand() {
		return "/register";
	}

	@Override
	public String getCommandExample() {
		return getCommand() + " Username ";
	}

	@Override
	public String getCommandDescription() {
		return "Зарегистрироваться в RPG-системе. Используйте ваш leetcode юзернейм! Использовать только в личном чате с ботом";
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.domain.exception.TelegramException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(2)
public class RegisterHandler implements CommandHandler {
	private final RegisterUserUseCase registerUserUseCase;

	public RegisterHandler(RegisterUserUseCase registerUserUseCase) {
		this.registerUserUseCase = registerUserUseCase;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return text.startsWith(getCommand());
	}

	@Override
	@Transactional
	public String handle(Update update) {
		String[] parts = update.getMessage().getText().trim().split("\\s+");

		if (parts.length != 2)
			throw new TelegramException.InvalidCommandException(getCommandExample());

		Long userId = update.getMessage().getFrom().getId();

		String leetcodeUsername = parts[1];

		registerUserUseCase.registerUser(userId, leetcodeUsername);

		return "Успешная регистрация!";
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
		return "Зарегистрироваться в RPG-системе. Используйте ваш leetcode юзернейм!";
	}
}

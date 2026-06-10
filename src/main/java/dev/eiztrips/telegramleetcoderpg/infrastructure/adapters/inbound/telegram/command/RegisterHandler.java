package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.domain.exception.TelegramException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RegisterHandler implements CommandHandler {
	private final RegisterUserUseCase registerUserUseCase;

	public RegisterHandler(RegisterUserUseCase registerUserUseCase) {
		this.registerUserUseCase = registerUserUseCase;
	}

	@Override
	public boolean canHandle(String text) {
		return text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		String[] parts = update.getMessage().getText().trim().split("\\s+");

		if (parts.length != 2)
			throw new TelegramException.InvalidCommandException(getCommandExample());

		// todo: добавить валидацию LeetCode пользователя

		Long userId = update.getMessage().getChatId();
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
		return getCommand() + " {LeetCode_никнейм} ";
	}
}

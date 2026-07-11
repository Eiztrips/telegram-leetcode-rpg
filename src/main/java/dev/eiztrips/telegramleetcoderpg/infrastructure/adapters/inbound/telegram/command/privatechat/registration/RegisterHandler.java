package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.registration;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.RegisterUserUseCase;
import dev.eiztrips.telegramleetcoderpg.domain.exception.TelegramExceptions;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.PrivateChatHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramPrivateMessagePresenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(2)
public class RegisterHandler extends PrivateChatHandler implements CommandHandler {
	private final RegisterUserUseCase registerUserUseCase;

	@Value("${spring.profiles.active:dev}")
	private String profile;

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
		Long userId = update.getMessage().getFrom().getId();
		String leetcodeUsername = extractLeetcodeUsername(update);

		if (profile.equals("dev"))
			return registerUserUseCase.createUser(userId, leetcodeUsername).toString();

		String token = registerUserUseCase.startUserRegistration(userId, leetcodeUsername);

		return TelegramPrivateMessagePresenter.formatRegisterInfo(token);
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

	private String extractLeetcodeUsername(Update update) {
		String[] parts = update.getMessage().getText().trim().split("\\s+");

		if (parts.length != 2)
			throw new TelegramExceptions.InvalidCommandException(getCommandExample());

		return parts[1];
	}
}

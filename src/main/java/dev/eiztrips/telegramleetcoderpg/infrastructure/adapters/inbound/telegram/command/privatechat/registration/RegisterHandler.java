package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.privatechat.registration;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.RegisterUserUseCase;
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
		String[] parts = update.getMessage().getText().trim().split("\\s+");

		if (parts.length != 2)
			throw new TelegramExceptions.InvalidCommandException(getCommandExample());

		Long userId = update.getMessage().getFrom().getId();
		String leetcodeUsername = parts[1];

		if (profile.equals("dev")) {
			registerUserUseCase.createUser(userId, leetcodeUsername);
			return "Пользователь создан";
		}

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
}

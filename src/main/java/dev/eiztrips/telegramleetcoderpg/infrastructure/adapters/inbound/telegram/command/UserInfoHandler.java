package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.service.user.UserService;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramCommonMessagePresenter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(4)
public class UserInfoHandler implements CommandHandler {

	private final UserService userService;
	private final UserRepositoryPort userRepositoryPort;

	public UserInfoHandler(UserService userService, UserRepositoryPort userRepositoryPort) {
		this.userService = userService;
		this.userRepositoryPort = userRepositoryPort;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		String[] parts = update.getMessage().getText().split("\\s+");
		String username = parts.length > 1 ? parts[0] : getLeetcodeUsername(update);

		UserInfoResult userInfoResult = userService.getUserInfo(username);

		return TelegramCommonMessagePresenter.formatUserInfo(userInfoResult);
	}

	private String getLeetcodeUsername(Update update) {
		return userRepositoryPort.getByTelegramId(update.getMessage().getFrom().getId())
				.orElseThrow(UserExceptions.UserNotFoundException::new).leetcodeUsername();
	}

	@Override
	public String getCommand() {
		return "/me";
	}

	@Override
	public String getCommandExample() {
		return getCommand() + " username";
	}

	@Override
	public String getCommandDescription() {
		return "Получить информацию о себе или другом пользователе.";
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.GetUserInfoUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.repository.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramCommonMessagePresenter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Component
@Order(4)
public class UserInfoHandler implements CommandHandler {

	private final UserRepositoryPort userRepositoryPort;
	private final GetUserInfoUseCase getUserInfoUseCase;

	public UserInfoHandler(UserRepositoryPort userRepositoryPort, GetUserInfoUseCase getUserInfoUseCase) {
		this.userRepositoryPort = userRepositoryPort;
		this.getUserInfoUseCase = getUserInfoUseCase;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return isAlias(text);
	}

	@Override
	public String handle(Update update) {
		String[] parts = update.getMessage().getText().split("\\s+");
		String username = parts.length > 1 ? parts[1] : getLeetcodeUsername(update);

		UserInfoResult userInfoResult = getUserInfoUseCase.getUserInfo(username);

		return TelegramCommonMessagePresenter.formatUserInfo(userInfoResult);
	}

	private String getLeetcodeUsername(Update update) {
		return userRepositoryPort.getByTelegramId(update.getMessage().getFrom().getId())
				.orElseThrow(UserExceptions.UserNotFoundException::new).leetcodeUsername();
	}

	@Override
	public String getCommand() {
		return "/profile";
	}

	@Override
	public Set<String> getCommandAliases() {
		return Set.of(getCommand(), "/me", "/user");
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

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.AddUserToGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.guild.CreateGuildUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(3)
public class CreateGuildHandler extends GroupChatHandler implements CommandHandler {

	private final CreateGuildUseCase createGuildUseCase;
	private final UserRepositoryPort userRepositoryPort;
	private final GuildRepositoryPort guildRepositoryPort;
	private final AddUserToGuildUseCase addUserToGuildUseCase;

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

		var info = new StringBuilder();

		guildRepositoryPort.getGuildById(chatId).ifPresentOrElse(g -> {
		}, () -> {
			createGuildUseCase.create(chatId);
			addUserToGuildUseCase.addUserToGuild(update.getMessage().getFrom().getId(), chatId);
			info.append("Успешное создание гильдии!\n\n");
		});

		info.append("<blockquote>");

		info.append(String.format("Гильдия - %s%n%n", update.getMessage().getChat().getTitle()));

		var boss = guildRepositoryPort.getCurrentWeeklyBoss(chatId);

		boss.ifPresent(b -> info
				.append(String.format("Текущий босс - %s | %d/%d hp%n%n", b.name(), b.maxHp(), b.currentHp())));

		var users = userRepositoryPort.getByGuildId(chatId);

		if (!users.isEmpty()) {
			info.append("Пользователи:\n");
			for (User u : users)
				info.append(String.format("• %s | %d id | %d xp%n", u.leetcodeUsername(), u.telegramId(), u.xp()));
		}

		info.append("</blockquote>");

		return info.toString();
	}

	@Override
	public String getCommand() {
		return "/guild";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return "Получить информацию или создать гильдию." + " Доступно только в общих чатах.";
	}
}

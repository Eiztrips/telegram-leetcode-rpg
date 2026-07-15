package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.ProcessAttackBossResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.boss.ProcessAttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramGuildMessagePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Order(6)
public class AttackBossHandler extends GroupChatHandler implements CommandHandler {

	private final ProcessAttackBossUseCase processAttackBossUseCase;

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && isAlias(text);
	}

	@Override
	public String handle(Update update) {
		Long userId = update.getMessage().getFrom().getId();
		ProcessAttackBossResult processAttackBossResult = processAttackBossUseCase.processAttackBoss(userId);

		return TelegramGuildMessagePresenter.formatProcessBossAttackInfo(processAttackBossResult.boss(),
				processAttackBossResult.submissionDataList());
	}

	@Override
	public String getCommand() {
		return "/attack";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}

	@Override
	public String getCommandDescription() {
		return """
				Атаковать босса.
				📍 Гильдия должна существовать!
				📍 Вы должны быть в гильдии!
				📍 Босс должен существовать!
				""";
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.boss.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter.TelegramGuildMessagePresenter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Order(6)
public class AttackBossHandler extends GroupChatHandler implements CommandHandler {

	private final AttackBossUseCase attackBossUseCase;
	private final CheckSubmissionsUseCase checkSubmissionsUseCase;

	public AttackBossHandler(AttackBossUseCase attackBossUseCase, CheckSubmissionsUseCase checkSubmissionsUseCase) {
		this.attackBossUseCase = attackBossUseCase;
		this.checkSubmissionsUseCase = checkSubmissionsUseCase;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		Long userId = update.getMessage().getFrom().getId();

		List<SubmissionData> submissionDataList = checkSubmissionsUseCase.checkTodaySubmissions(userId);

		if (submissionDataList.isEmpty())
			return TelegramGuildMessagePresenter.formatProcessBossAttackInfo(null, null);

		var boss = attackBossUseCase.attackBoss(submissionDataList, userId);

		return TelegramGuildMessagePresenter.formatProcessBossAttackInfo(boss, submissionDataList);
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
		return "Атаковать босса. Доступно только в общих чатах," + " при условии что вы находитесь в гильдии,"
				+ " гильдия и босс существуют.";
	}
}

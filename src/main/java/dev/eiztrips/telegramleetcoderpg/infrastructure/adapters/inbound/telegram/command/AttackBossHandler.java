package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class AttackBossHandler implements CommandHandler {

	private final AttackBossUseCase attackBossUseCase;
	private final CheckSubmissionsUseCase checkSubmissionsUseCase;

	public AttackBossHandler(AttackBossUseCase attackBossUseCase, CheckSubmissionsUseCase checkSubmissionsUseCase) {
		this.attackBossUseCase = attackBossUseCase;
		this.checkSubmissionsUseCase = checkSubmissionsUseCase;
	}

	@Override
	public boolean canHandle(String text) {
		return text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		Long userId = update.getMessage().getChatId();

		List<SubmissionData> submissionDataList;

		submissionDataList = checkSubmissionsUseCase.checkTodaySubmissions(userId);

		Long CHANGE_ME_LATER = 0L; // fixme

		WeeklyBoss newBossState = attackBossUseCase.attackBoss(CHANGE_ME_LATER, submissionDataList);

		if (newBossState.currentHp() == 0) {
			return ("Успешно проведена атака %d задачами. Босс повержен!");
		} else {
			return String.format("Успешно проведена атака %d задачами. Здоровье босса: %d/%d",
					submissionDataList.size(), newBossState.currentHp(), newBossState.maxHp());
		}
	}

	@Override
	public String getCommand() {
		return "/attack";
	}

	@Override
	public String getCommandExample() {
		return getCommand();
	}
}

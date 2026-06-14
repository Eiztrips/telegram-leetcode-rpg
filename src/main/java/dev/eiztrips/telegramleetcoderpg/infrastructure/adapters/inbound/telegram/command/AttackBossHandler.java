package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AttackBossHandler implements CommandHandler {

	private final AttackBossUseCase attackBossUseCase;
	private final CheckSubmissionsUseCase checkSubmissionsUseCase;

	public AttackBossHandler(AttackBossUseCase attackBossUseCase, CheckSubmissionsUseCase checkSubmissionsUseCase) {
		this.attackBossUseCase = attackBossUseCase;
		this.checkSubmissionsUseCase = checkSubmissionsUseCase;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return text.startsWith(getCommand());
	}

	@Override
	public String handle(Update update) {
		Long userId = update.getMessage().getFrom().getId();

		List<SubmissionData> submissionDataList;

		submissionDataList = checkSubmissionsUseCase.checkTodaySubmissions(userId);

		Long CHANGE_ME_LATER = 1L; // fixme

		WeeklyBoss newBossState = attackBossUseCase.attackBoss(CHANGE_ME_LATER, submissionDataList);

		StringBuilder sb = new StringBuilder();

		if (newBossState.currentHp() == 0) {
			sb.append(String.format("Успешно проведена атака %d задачами. Босс повержен!", submissionDataList.size()));
		} else {
			sb.append(String.format("Успешно проведена атака %d задачами. Здоровье босса: %d/%d",
					submissionDataList.size(), newBossState.currentHp(), newBossState.maxHp()));
		}

		sb.append("\n\nПроведенные атаки:\n");
		submissionDataList.forEach(submissionData -> sb.append(String.format("- %s (%s)%n", submissionData.taskTitle(),
				"https://leetcode.com/problems/" + submissionData.taskSlug())));

		return sb.toString();
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

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

import java.util.List;

public class TelegramGuildMessagePresenter {
	private TelegramGuildMessagePresenter() {
	}

	public static String formatGuildInfo(String title, boolean isCreated, WeeklyBoss boss, List<User> users) {
		var info = new StringBuilder();

		if (isCreated)
			info.append("<b>Успешное создание гильдии!</b>\n\n");

		info.append("<blockquote>");
		info.append(String.format("<b>%s</b>%n%n", title));

		if (boss != null)
			info.append(String.format("<b>Текущий босс:</b> %n <i>%s | %d/%d hp%n%n</i>", boss.name(), boss.currentHp(),
					boss.maxHp()));

		if (!users.isEmpty()) {
			info.append("<b>Пользователи:</b>\n");

			for (User u : users)
				info.append(
						String.format("<i>• %s | %d id | %d xp%n</i>", u.leetcodeUsername(), u.telegramId(), u.xp()));
		}

		info.append("</blockquote>");

		return info.toString();
	}

	public static String formatProcessBossAttackInfo(WeeklyBoss newBossState, List<SubmissionData> submissionDataList) {
		var info = new StringBuilder();

		if (submissionDataList == null)
			return "Вы не выполнили ни одной задачи!";

		if (newBossState.currentHp() == 0) {
			info.append(
					String.format("Успешно проведена атака %d задачами. Босс повержен!", submissionDataList.size()));
		} else {
			info.append(String.format("Успешно проведена атака %d задачами. Здоровье босса: %d/%d",
					submissionDataList.size(), newBossState.currentHp(), newBossState.maxHp()));
		}

		info.append("\n\nПроведенные атаки:\n");
		submissionDataList.forEach(submissionData -> info.append(String.format("- %s (%s)%n",
				submissionData.taskTitle(), "https://leetcode.com/problems/" + submissionData.taskSlug())));

		return info.toString();
	}
}

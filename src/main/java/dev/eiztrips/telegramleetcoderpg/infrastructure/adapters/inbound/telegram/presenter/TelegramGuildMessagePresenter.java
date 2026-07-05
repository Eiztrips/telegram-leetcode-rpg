package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

import java.util.List;

public class TelegramGuildMessagePresenter {
	private TelegramGuildMessagePresenter() {
	}

	public static String formatGuildInfo(String title, boolean isCreated, WeeklyBoss boss, List<User> users) {
		String header = isCreated ? "<b>Успешное создание гильдии!</b>\n\n" : "";

		String bossSection = (boss == null) ? "" : """
				<b>Текущий босс:</b>
				<i>%s | %d/%d hp</i>

				""".formatted(boss.name(), boss.currentHp(), boss.maxHp());

		String usersSection = users.isEmpty()
				? ""
				: "<b>Пользователи:</b>\n" + users.stream()
						.map(u -> "<i>• %s | %d id | %d xp</i>".formatted(u.leetcodeUsername(), u.telegramId(), u.xp()))
						.collect(java.util.stream.Collectors.joining("\n"));

		return """
				%s<blockquote><b>%s</b>

				%s%s</blockquote>""".formatted(header, title, bossSection, usersSection).trim();
	}

	public static String formatProcessBossAttackInfo(WeeklyBoss newBossState, List<SubmissionData> submissionDataList) {
		if (submissionDataList == null || submissionDataList.isEmpty()) {
			return "Вы не выполнили ни одной задачи!";
		}

		String status = (newBossState.currentHp() == 0)
				? "Успешно проведена атака %d задачами. Босс повержен!".formatted(submissionDataList.size())
				: "Успешно проведена атака %d задачами. Здоровье босса: %d/%d".formatted(submissionDataList.size(),
						newBossState.currentHp(), newBossState.maxHp());

		String attacks = submissionDataList.stream()
				.map(sub -> "- %s (https://leetcode.com/problems/%s)".formatted(sub.taskTitle(), sub.taskSlug()))
				.collect(java.util.stream.Collectors.joining("\n"));

		return """
				%s

				Проведенные атаки:
				%s""".formatted(status, attacks).trim();
	}
}

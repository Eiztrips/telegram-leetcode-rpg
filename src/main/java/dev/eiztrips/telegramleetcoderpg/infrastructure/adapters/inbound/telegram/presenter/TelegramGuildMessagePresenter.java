package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

import java.util.List;

public class TelegramGuildMessagePresenter {
	private TelegramGuildMessagePresenter() {
	}

	public static String formatGuildInfo(String title, boolean isCreated, WeeklyBoss boss, List<User> users) {
		StringBuilder sb = new StringBuilder();

		if (isCreated) {
			sb.append("<b>Гильдия создана!</b>\n\n");
		} else {
			sb.append("🏰 <b>Информация о гильдии</b>\n");
		}

		sb.append("<blockquote>");
		sb.append("🏛️ <b>%s</b>\n\n".formatted(title));

		if (boss != null) {
			int hpPercent = boss.maxHp() > 0 ? (boss.currentHp() * 100 / boss.maxHp()) : 0;
			sb.append("💀 <b>Босс:</b> %s\n".formatted(boss.name()));
			sb.append("❤️ HP: %d / %d  (%d%%)\n\n".formatted(boss.currentHp(), boss.maxHp(), hpPercent));
		}

		if (!users.isEmpty()) {
			sb.append("👥 <b>Участники (%d):</b>\n".formatted(users.size()));
			for (User u : users) {
				sb.append("   • <a href=\"https://leetcode.com/u/%s/\">%s</a> — <b>%s</b>\n"
						.formatted(u.leetcodeUsername(), u.leetcodeUsername(), u.getRank().getTitle()));
			}
		}

		sb.append("</blockquote>");

		return sb.toString().trim();
	}

	public static String formatProcessBossAttackInfo(WeeklyBoss newBossState, List<SubmissionData> submissionDataList) {
		if (submissionDataList == null || submissionDataList.isEmpty()) {
			return "⚠️ Вы не выполнили ни одной задачи для атаки!";
		}

		StringBuilder sb = new StringBuilder();

		boolean bossDefeated = newBossState.currentHp() == 0;

		if (bossDefeated) {
			sb.append("🏆 <b>Босс повержен!</b>\n");
			sb.append("Атака силой в <b>%d</b> задач!\n".formatted(submissionDataList.size()));
		} else {
			sb.append("⚔️ <b>Атака проведена!</b>\n");
			sb.append("Сила атаки: <b>%d</b> задач\n".formatted(submissionDataList.size()));
		}

		sb.append("\n");
		sb.append("<blockquote>");

		int attack = submissionDataList.stream()
				.mapToInt(submissionData -> Difficulty.valueOf(submissionData.taskDifficulty()).getReward()).sum();

		if (!bossDefeated) {
			int hpPercent = newBossState.maxHp() > 0 ? (newBossState.currentHp() * 100 / newBossState.maxHp()) : 0;
			sb.append("💀 <b>%s</b>\n".formatted(newBossState.name()));
			sb.append("❤️ HP: %d / %d  (%d%%) -%d hp\n\n".formatted(newBossState.currentHp(), newBossState.maxHp(),
					hpPercent, attack));
		}

		sb.append("🗡️ <b>Использованные задачи:</b>\n");
		for (SubmissionData sub : submissionDataList) {
			sb.append("   • <a href=\"https://leetcode.com/submissions/detail/%s/\">%s</a>\n"
					.formatted(sub.submissionId(), sub.taskTitle()));
		}

		sb.append("</blockquote>");

		return sb.toString().trim();
	}
}

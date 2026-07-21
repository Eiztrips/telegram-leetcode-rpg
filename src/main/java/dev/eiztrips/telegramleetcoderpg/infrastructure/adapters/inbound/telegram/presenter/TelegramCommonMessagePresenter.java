package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelegramCommonMessagePresenter {

	private TelegramCommonMessagePresenter() {
	}

	public static String formatUserInfo(UserInfoResult userInfoResult) {
		User user = userInfoResult.user();
		Guild guild = userInfoResult.guild();
		List<Submission> submissions = userInfoResult.submissions();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
				.withZone(ZoneId.systemDefault());

		StringBuilder sb = new StringBuilder();

		sb.append("⚔️ <b>Боевой профиль</b>\n");
		sb.append("\n");
		sb.append("<blockquote>");
		sb.append("👤 <a href=\"https://leetcode.com/u/%s/\">%s</a>\n".formatted(user.leetcodeUsername(),
				user.leetcodeUsername()));
		sb.append("✨ Ранг: <b>%s</b> <i>(%d lvl | %d xp)</i>\n".formatted(user.getRank().getTitle(), user.getLevel(),
				user.xp()));

		if (guild != null) {
			sb.append("🏰 Гильдия: <i>%s</i>\n".formatted(guild.id()));
		}

		if (!submissions.isEmpty()) {
			sb.append("\n");
			sb.append("📋 <b>Последняя отправка:</b> %s\n".formatted(formatter.format(user.lastCheckTime())));
			sb.append("📜 <b>Решённые задачи за неделю:</b>\n");
			for (Submission s : submissions) {
				sb.append("   • <a href=\"https://leetcode.com/submissions/detail/%s/\">%s</a> — <b>%d</b> xp\n"
						.formatted(s.submissionId(), s.taskTitle(), s.getReward()));
			}
		}

		sb.append("</blockquote>");

		return sb.toString().trim();
	}

	public static String formatHelpInfo(List<CommandHandler> privateCommandHandlers,
			List<CommandHandler> groupCommandHandlers, List<CommandHandler> commonCommandHandlers

	) {
		StringBuilder sb = new StringBuilder();

		sb.append("⚔️ <b>Добро пожаловать в мир RPG LeetCode!</b>\n");
		sb.append("\n");
		sb.append("<i>нажмите на блок что бы увидеть список комманд</i>\n");

		sb.append("\n");
		sb.append("<blockquote expandable>");
		sb.append("🔐 <b>ЛИЧНЫЕ КОМАНДЫ</b>\n");
		sb.append("данные команды можно использовать только в чате с ботом\n");
		for (CommandHandler h : privateCommandHandlers) {
			sb.append("\n");
			sb.append("  <b>%s</b>\n".formatted(h.getCommandExample()));
			sb.append("    <i>%s</i>\n".formatted(h.getCommandDescription()));
		}
		sb.append("</blockquote>");

		sb.append("\n");
		sb.append("<blockquote expandable>");
		sb.append("🏰 <b>ГРУППОВЫЕ КОМАНДЫ</b>\n");
		sb.append("данные команды можно использовать только в беседах\n");
		for (CommandHandler h : groupCommandHandlers) {
			sb.append("\n");
			sb.append("  <b>%s</b>\n".formatted(h.getCommandExample()));
			sb.append("    <i>%s</i>\n".formatted(h.getCommandDescription()));
		}
		sb.append("</blockquote>");

		sb.append("\n");
		sb.append("<blockquote expandable>");
		sb.append("🌍 <b>ОБЩИЕ КОМАНДЫ</b>\n");
		sb.append("данные команды можно использовать во всех чатах\n");
		for (CommandHandler commandHandler : commonCommandHandlers) {
			if (!privateCommandHandlers.contains(commandHandler) && !groupCommandHandlers.contains(commandHandler)) {
				sb.append("\n");
				sb.append("  <b>%s</b>\n".formatted(commandHandler.getCommandExample()));
				sb.append("    <i>%s</i>\n".formatted(commandHandler.getCommandDescription()));
			}
		}
		sb.append("</blockquote>");

		return sb.toString();
	}
}

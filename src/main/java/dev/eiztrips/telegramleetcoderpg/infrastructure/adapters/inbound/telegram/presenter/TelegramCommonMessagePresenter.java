package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.presenter;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.UserInfoResult;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TelegramCommonMessagePresenter {

	private TelegramCommonMessagePresenter() {
	}

	public static String formatUserInfo(UserInfoResult userInfoResult) {
		User user = userInfoResult.user();
		Guild guild = userInfoResult.guild();
		List<Submission> submissions = userInfoResult.submissions();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
				.withZone(ZoneId.systemDefault());

		String header = "<b>Пользователь</b> <a href=\"https://leetcode.com/u/%s/\">%s</a> | %s"
				.formatted(user.leetcodeUsername(), user.leetcodeUsername(), user.telegramId().toString());

		String guildSection = (guild == null) ? "" : "<b>Гильдия:</b> <i>%s</i>".formatted(guild.id());

		String submissionSection = submissions.isEmpty()
				? ""
				: """
						<b>Последняя отправка:</b> %s
						<b>Реш/енные задачи за последнюю неделю:</b>%n%s
						""".formatted(formatter.format(user.lastCheckTime()), submissions.stream().map(
						submission -> "<i>• <a href=\"https://leetcode.com/submissions/detail/%s/\">%s</a> | %d xp</i>"
								.formatted(submission.taskTitle(), submission.submissionId(), submission.getReward()))
						.collect(Collectors.joining("\n")));

		return """
				%s%n%n<blockquote>%s%n%n%s</blockquote>
				""".formatted(header, guildSection, submissionSection).trim();
	}
}

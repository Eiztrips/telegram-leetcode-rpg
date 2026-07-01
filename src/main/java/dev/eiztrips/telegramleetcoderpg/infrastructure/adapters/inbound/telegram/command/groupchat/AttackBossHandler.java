package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.groupchat;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.boss.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.guild.GuildRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.GuildExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram.command.CommandHandler;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Order(6)
public class AttackBossHandler extends GroupChatHandler implements CommandHandler {

	private final AttackBossUseCase attackBossUseCase;
	private final CheckSubmissionsUseCase checkSubmissionsUseCase;
	private final GuildRepositoryPort guildRepositoryPort;
	private final UserRepositoryPort userRepositoryPort;

	public AttackBossHandler(AttackBossUseCase attackBossUseCase, CheckSubmissionsUseCase checkSubmissionsUseCase,
			GuildRepositoryPort guildRepositoryPort, UserRepositoryPort userRepositoryPort) {
		this.attackBossUseCase = attackBossUseCase;
		this.checkSubmissionsUseCase = checkSubmissionsUseCase;
		this.guildRepositoryPort = guildRepositoryPort;
		this.userRepositoryPort = userRepositoryPort;
	}

	@Override
	public boolean canHandle(Update update) {
		String text = update.getMessage().getText();
		return super.canHandle(update) && text.startsWith(getCommand());
	}

	@Override
	@Transactional
	public String handle(Update update) {
		Long userId = update.getMessage().getFrom().getId();

		List<SubmissionData> submissionDataList = checkSubmissionsUseCase.checkTodaySubmissions(userId);

		if (submissionDataList.isEmpty())
			return "Вы не выполнили ни одной задачи";

		var userGuild = userRepositoryPort.getGuildByUserTelegramId(userId)
				.orElseThrow(UserExceptions.UserGuildNotFoundException::new);

		var guildBoss = guildRepositoryPort.getCurrentWeeklyBoss(userGuild.id())
				.orElseThrow(GuildExceptions.GuildBossNotFountException::new);

		WeeklyBoss newBossState = attackBossUseCase.attackBoss(guildBoss.id(), submissionDataList);

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

	@Override
	public String getCommandDescription() {
		return "Атаковать босса. Доступно только в общих чатах," + " при условии что вы находитесь в гильдии,"
				+ " гильдия и босс существуют.";
	}
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.scheduler;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.InactiveUserResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.CheckUserInactiveUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.client.chat.ChatClientPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class UserInactiveAlertScheduler {

	private final CheckUserInactiveUseCase checkUserInactiveUseCase;
	private final Integer inactiveDaysAlarm;
	private final Integer inactiveDaysDelete;
	private final ChatClientPort chatClientPort;

	public UserInactiveAlertScheduler(CheckUserInactiveUseCase checkUserInactiveUseCase, ChatClientPort chatClientPort,
			@Value("${inactive.days.alarm:null}") Integer inactiveDaysAlarm,
			@Value("${inactive.days.delete:null}") Integer inactiveDaysDelete) {
		this.checkUserInactiveUseCase = checkUserInactiveUseCase;
		this.chatClientPort = chatClientPort;
		this.inactiveDaysAlarm = inactiveDaysAlarm;
		this.inactiveDaysDelete = inactiveDaysDelete;
	}

	@Scheduled(cron = "0 0 0 */2 * *")
	public void cronCheckUsersInactive() {
		List<InactiveUserResult> inactiveUsers = checkUserInactiveUseCase.checkAllUsersInactive(inactiveDaysDelete,
				inactiveDaysAlarm);
		inactiveUsers.forEach(user -> {
			if (user.isDeleted()) {
				chatClientPort.sendMessage(user.user().telegramId(), "<b>Вы были удалены из-за неактивности!</b>");
				return;
			}

			if (user.user().lastCheckTime() == null) {
				chatClientPort.sendMessage(user.user().telegramId(),
						"<b>Начните решать задачи или ваш аккаунт будет удален при следующей проверке! </b>");
				return;
			}

			long time = inactiveDaysDelete - ChronoUnit.DAYS.between(user.user().lastCheckTime(), Instant.now());
			String alertMessage = time > 0
					? "<b>Решайте задачи, иначе ваш аккаунт будет удален через: %d дней!</b>".formatted(time)
					: "<b>Быстрее! Решите задачу, иначе после следующей проверки ваш аккаунт будет удален!</b>";

			if (user.isInactive())
				chatClientPort.sendMessage(user.user().telegramId(), alertMessage);
		});

		int usersInactive = 0, usersDeleted = 0;
		for (InactiveUserResult u : inactiveUsers)
			if (u.isDeleted())
				usersDeleted++;
			else if (u.isInactive())
				usersInactive++;

		log.info("Проверка пользователей: {} всего, {} активно, {} инактивно, {} удалено", inactiveUsers.size(),
				inactiveUsers.size() - usersInactive - usersDeleted, usersInactive, usersDeleted);
	}
}

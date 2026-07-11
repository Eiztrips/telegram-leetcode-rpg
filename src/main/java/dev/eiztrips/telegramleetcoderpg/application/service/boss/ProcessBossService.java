package dev.eiztrips.telegramleetcoderpg.application.service.boss;

import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.boss.AttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.boss.ProcessAttackBossUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto.ProcessAttackBossResult;
import dev.eiztrips.telegramleetcoderpg.application.ports.inbound.usecase.user.CheckSubmissionsUseCase;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис обработки сценариев босса.
 */
@Service
public class ProcessBossService implements ProcessAttackBossUseCase {
	private final CheckSubmissionsUseCase checkSubmissionsUseCase;
	private final AttackBossUseCase attackBossUseCase;

	public ProcessBossService(CheckSubmissionsUseCase checkSubmissionsUseCase, AttackBossUseCase attackBossUseCase) {
		this.checkSubmissionsUseCase = checkSubmissionsUseCase;
		this.attackBossUseCase = attackBossUseCase;
	}

	@Override
	@Transactional
	public ProcessAttackBossResult processAttackBoss(Long userId) {
		List<SubmissionData> submissionDataList = checkSubmissionsUseCase.checkTodaySubmissions(userId);

		if (submissionDataList.isEmpty())
			return ProcessAttackBossResult.builder().build();

		var boss = attackBossUseCase.attackBoss(submissionDataList, userId);

		return ProcessAttackBossResult.builder().boss(boss).submissionDataList(submissionDataList).build();
	}
}

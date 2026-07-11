package dev.eiztrips.telegramleetcoderpg.application.ports.inbound.dto;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import lombok.Builder;

import java.util.List;

/**
 * Результат атаки босса
 *
 * @param boss
 *            босс
 * @param submissionDataList
 *            список задач при атаке
 */
public record ProcessAttackBossResult(WeeklyBoss boss, List<SubmissionData> submissionDataList) {
	@Builder
	public ProcessAttackBossResult {
	}
}

package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto;

import lombok.Builder;

import java.time.Instant;

/**
 * Общий Submission DTO для LeetCodeClientPort и UserRegistrationPort
 */
public record SubmissionData(Long submissionId, String taskTitle, String taskSlug, String taskDifficulty,
		Instant completedAt) {
	/**
	 * @param submissionId
	 *            индетификатор сабмишена
	 * @param taskTitle
	 *            название задачи
	 * @param taskSlug
	 *            уникальное название задачи
	 * @param taskDifficulty
	 *            сложность задачи
	 * @param completedAt
	 *            время выполнения задачи
	 */
	@Builder
	public SubmissionData {
	}
}

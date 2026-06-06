package dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto;

import java.time.Instant;

/**
 * Общий Submission DTO для LeetCodeClientPort и UserRegistrationPort
 */
public record SubmissionData(Long submissionId, String taskTitle, String taskSlug, String taskDifficulty,
		Instant completedAt) {
}

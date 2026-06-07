package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "user_submissions")
public class SubmissionEntity {

	@Id
	@EqualsAndHashCode.Include
	private Long submissionId;

	@Column(name = "task_title", nullable = false)
	private String taskTitle;

	@Column(name = "task_slug", nullable = false)
	private String taskSlug;

	@Column(name = "task_difficulty", nullable = false)
	private String taskDifficulty;

	@Column(name = "completed_at", nullable = false)
	private Instant completedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
}

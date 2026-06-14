package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "users")
public class UserEntity {

	@Id
	@EqualsAndHashCode.Include
	private Long telegramId;

	@Column(name = "leetcode_username", nullable = false, unique = true)
	private String leetcodeUsername;

	@Column(name = "experience")
	private int xp;

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<SubmissionEntity> submissions = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "guild_id")
	private GuildEntity guild;

	@Column(name = "last_submission_check_time")
	private Instant lastCheckTime;

	public void addSubmissions(List<SubmissionEntity> newSubmissions) {
		if (newSubmissions == null || newSubmissions.isEmpty())
			return;

		for (SubmissionEntity submission : newSubmissions) {
			submission.setUser(this);
			this.submissions.add(submission);
		}
	}
}

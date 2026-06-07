package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "weekly_bosses")
public class WeeklyBossEntity {

	@Version
	private Long version;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weekly_boss_seq")
	@SequenceGenerator(name = "weekly_boss_seq", sequenceName = "weekly_bosses_id_seq", allocationSize = 1)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "max_hp", nullable = false)
	private int maxHp;

	@Column(name = "current_hp", nullable = false)
	private int currentHp;
}

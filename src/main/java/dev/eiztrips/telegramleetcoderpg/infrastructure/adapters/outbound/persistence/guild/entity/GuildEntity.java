package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Table(name = "guilds")
public class GuildEntity {
	@Version
	Long version;

	@Id
	Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boss_id")
	WeeklyBossEntity currentBoss;
}

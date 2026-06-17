package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
	@Query("SELECT u.guild FROM UserEntity u WHERE u.telegramId = :id")
	Optional<GuildEntity> findGuildByTelegramId(@Param("id") Long telegramId);
}

package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.spring;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SpringDataSubmissionRepository extends JpaRepository<SubmissionEntity, Long> {
	@Query("SELECT s FROM SubmissionEntity s WHERE s.user.telegramId = :telegramId AND s.completedAt >= :since")
	List<SubmissionEntity> findWeeklySubmissions(@Param("telegramId") Long telegramId, @Param("since") Instant since);
}

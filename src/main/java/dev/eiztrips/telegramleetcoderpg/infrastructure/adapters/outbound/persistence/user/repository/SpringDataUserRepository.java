package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository;

import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
}

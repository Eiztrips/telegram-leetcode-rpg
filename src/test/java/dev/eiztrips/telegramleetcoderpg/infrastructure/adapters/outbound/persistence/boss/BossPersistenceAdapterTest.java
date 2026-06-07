package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss;

import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.entity.WeeklyBossEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.mapper.WeeklyBossMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.boss.repository.SpringDataBossRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BossPersistenceAdapterTest {

    @Mock
    private SpringDataBossRepository bossRepository;

    @Mock
    private WeeklyBossMapper weeklyBossMapper;

    @InjectMocks
    private BossPersistenceAdapter bossPersistenceAdapter;

    @Test
    void save_ShouldMapAndSaveEntity() {
        WeeklyBoss domainBoss = mock(WeeklyBoss.class);
        WeeklyBossEntity entityBoss = mock(WeeklyBossEntity.class);

        when(weeklyBossMapper.toEntity(domainBoss)).thenReturn(entityBoss);

        bossPersistenceAdapter.save(domainBoss);

        verify(weeklyBossMapper).toEntity(domainBoss);
        verify(bossRepository).save(entityBoss);
    }

    @Test
    void getById_WhenBossExists_ShouldReturnMappedDomainBoss() {
        Long bossId = 1L;
        WeeklyBossEntity entityBoss = mock(WeeklyBossEntity.class);
        WeeklyBoss domainBoss = mock(WeeklyBoss.class);

        when(bossRepository.findById(bossId)).thenReturn(Optional.of(entityBoss));
        when(weeklyBossMapper.toDomain(entityBoss)).thenReturn(domainBoss);

        Optional<WeeklyBoss> result = bossPersistenceAdapter.getById(bossId);

        assertThat(result).isPresent().contains(domainBoss);
        verify(bossRepository).findById(bossId);
        verify(weeklyBossMapper).toDomain(entityBoss);
    }

    @Test
    void getById_WhenBossDoesNotExist_ShouldReturnEmptyOptional() {
        Long bossId = 1L;
        when(bossRepository.findById(bossId)).thenReturn(Optional.empty());

        Optional<WeeklyBoss> result = bossPersistenceAdapter.getById(bossId);

        assertThat(result).isEmpty();
        verify(bossRepository).findById(bossId);
        verifyNoInteractions(weeklyBossMapper);
    }
}
package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions.WeeklyBossNotFoundException;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование сервиса боя с боссом (BossFightService)")
class BossFightServiceTest {

    @Mock
    private BossRepositoryPort bossRepositoryPort;

    @InjectMocks
    private BossFightService bossFightService;

    private final Long bossId = 42L;

    @Test
    @DisplayName("Выброс исключения, если босс с указанным ID не найден")
    void attackBoss_WhenBossNotFound_ShouldThrowWeeklyBossNotFoundException() {
        int damage = 10;
        when(bossRepositoryPort.getById(bossId)).thenReturn(Optional.empty());

        assertThrows(WeeklyBossNotFoundException.class, () ->
                bossFightService.attackBoss(bossId, damage)
        );

        verify(bossRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Успешное нанесение урона боссу")
    void attackBoss_WhenBossExists_ShouldApplyDamageAndSave() {
        int damage = 50;

        WeeklyBoss initialBossMock = mock(WeeklyBoss.class);
        WeeklyBoss damagedBossMock = mock(WeeklyBoss.class);

        when(bossRepositoryPort.getById(bossId)).thenReturn(Optional.of(initialBossMock));

        when(initialBossMock.takeDamage(damage)).thenReturn(damagedBossMock);

        WeeklyBoss result = bossFightService.attackBoss(bossId, damage);

        assertNotNull(result, "Результат боя не должен быть null");
        assertEquals(damagedBossMock, result, "Сервис должен вернуть именно обновленного босса");

        verify(initialBossMock, times(1)).takeDamage(damage);

        verify(bossRepositoryPort, times(1)).save(damagedBossMock);
    }
}
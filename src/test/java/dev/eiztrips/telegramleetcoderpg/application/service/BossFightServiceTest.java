package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.boss.BossRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.WeeklyBossExceptions.WeeklyBossNotFoundException;
import dev.eiztrips.telegramleetcoderpg.domain.model.boss.WeeklyBoss;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        List<SubmissionData> emptySubmissions = List.of();
        when(bossRepositoryPort.getById(bossId)).thenReturn(Optional.empty());

        assertThrows(WeeklyBossNotFoundException.class, () ->
                bossFightService.attackBoss(bossId, emptySubmissions)
        );

        verify(bossRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Успешное нанесение урона боссу на основе списка посылок LeetCode")
    void attackBoss_WhenBossExists_ShouldCalculateDamageApplyAndSave() {
        SubmissionData submission1 = mock(SubmissionData.class);
        SubmissionData submission2 = mock(SubmissionData.class);

        when(submission1.taskDifficulty()).thenReturn("EASY");
        when(submission2.taskDifficulty()).thenReturn("MEDIUM");

        List<SubmissionData> submissions = List.of(submission1, submission2);

        int expectedDamage = dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty.EASY.getReward()
                + dev.eiztrips.telegramleetcoderpg.domain.model.user.Difficulty.MEDIUM.getReward();

        WeeklyBoss initialBossMock = mock(WeeklyBoss.class);
        WeeklyBoss damagedBossMock = mock(WeeklyBoss.class);

        when(bossRepositoryPort.getById(bossId)).thenReturn(Optional.of(initialBossMock));
        when(initialBossMock.takeDamage(expectedDamage)).thenReturn(damagedBossMock);

        WeeklyBoss result = bossFightService.attackBoss(bossId, submissions);

        assertNotNull(result, "Результат боя не должен быть null");
        assertEquals(damagedBossMock, result, "Сервис должен вернуть именно обновленного босса");

        verify(initialBossMock, times(1)).takeDamage(expectedDamage);
        verify(bossRepositoryPort, times(1)).save(damagedBossMock);
    }
}

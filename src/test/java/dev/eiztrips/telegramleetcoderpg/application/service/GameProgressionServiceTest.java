package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.leetcode.LeetCodeClientPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserNotFoundException;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование сервиса игрового прогресса (GameProgressionService)")
class GameProgressionServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private LeetCodeClientPort leetCodeClient;

    @Mock
    private User userMock;

    @InjectMocks
    private GameProgressionService gameProgressionService;

    private final Long telegramId = 12345L;

    @BeforeEach
    void setUp() {
        lenient().when(userMock.withLastCheckTime()).thenReturn(userMock);
    }

    @Test
    @DisplayName("Выброс исключения, если пользователь не найден в репозитории")
    void checkTodaySubmissions_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.getByTelegramId(telegramId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                gameProgressionService.checkTodaySubmissions(telegramId)
        );

        verifyNoInteractions(leetCodeClient);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Возврат false, если у пользователя нет новых отправленных решений за сегодня")
    void checkTodaySubmissions_WhenNoNewSubmissions_ShouldReturnFalse() {
        when(userRepository.getByTelegramId(telegramId)).thenReturn(Optional.of(userMock));

        SubmissionData duplicateSubmission = new SubmissionData(1L, "Two sum", "two-sum", "EASY", Instant.now());

        when(leetCodeClient.getTodaySubmissions(userMock.leetcodeUsername())).thenReturn(List.of(duplicateSubmission));
        when(userRepository.getSubmissionsLastWeek(telegramId)).thenReturn(List.of(duplicateSubmission));

        boolean result = gameProgressionService.checkTodaySubmissions(telegramId);

        assertFalse(result, "Метод должен вернуть false, так как новых задач нет");

        verify(userMock, times(1)).validateCheckRateLimit();

        verify(userMock, times(1)).withLastCheckTime();
        verify(userRepository, times(1)).save(userMock);

        verify(userMock, never()).takeRewardForSolveTask(anyList());
        verify(userRepository, never()).addSubmissions(anyLong(), anyList());
    }

    @Test
    @DisplayName("Успешное начисление награды, если найдены новые уникальные решения")
    void checkTodaySubmissions_WhenNewSubmissionsExist_ShouldRewardUserAndReturnTrue() {
        when(userRepository.getByTelegramId(telegramId)).thenReturn(Optional.of(userMock));

        SubmissionData oldSubmission = new SubmissionData(1L, "Two sum", "two-sum", "EASY", Instant.now().minus(Duration.ofDays(2)));
        SubmissionData newSubmission = new SubmissionData(2L, "Add two numbers", "add-two-numbers", "MEDIUM", Instant.now());

        when(leetCodeClient.getTodaySubmissions(userMock.leetcodeUsername())).thenReturn(List.of(oldSubmission, newSubmission));
        when(userRepository.getSubmissionsLastWeek(telegramId)).thenReturn(List.of(oldSubmission));

        User updatedUserMock = mock(User.class);
        when(userMock.takeRewardForSolveTask(anyList())).thenReturn(updatedUserMock);
        when(updatedUserMock.withLastCheckTime()).thenReturn(updatedUserMock);

        boolean result = gameProgressionService.checkTodaySubmissions(telegramId);

        assertTrue(result, "Метод должен вернуть true, так как была найдена новая задача");

        verify(userRepository, times(1)).addSubmissions(eq(telegramId), argThat(list ->
                list.size() == 1 && list.get(0).taskSlug().equals("add-two-numbers")
        ));

        verify(userMock, times(1)).takeRewardForSolveTask(anyList());
        verify(userRepository, times(1)).save(updatedUserMock);
    }
}
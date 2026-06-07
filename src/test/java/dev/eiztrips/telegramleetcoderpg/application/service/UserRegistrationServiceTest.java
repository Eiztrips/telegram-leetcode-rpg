package dev.eiztrips.telegramleetcoderpg.application.service;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions.UserAlreadyExistsException;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование сервиса регистрации пользователей (UserRegistrationService)")
class UserRegistrationServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    void registerUser_WhenUserDoesNotExist_ShouldRegisterSuccessfully() {
        Long telegramId = 123456789L;
        String name = "TestCoder";
        String leetcodeUrl = "https://leetcode.com/testcoder";

        when(userRepository.existsByTelegramId(telegramId)).thenReturn(false);

        User registeredUser = userRegistrationService.registerUser(telegramId, name, leetcodeUrl);

        assertNotNull(registeredUser, "Возвращенный пользователь не должен быть null");
        assertEquals(telegramId, registeredUser.telegramId());
        assertEquals(name, registeredUser.username());
        assertEquals(leetcodeUrl, registeredUser.leetcodeURL());
        assertEquals(0, registeredUser.xp(), "Начальный опыт должен быть равен 0");
        assertNotNull(registeredUser.lastCheckTime(), "Время проверки должно быть null при регистрации");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(telegramId, savedUser.telegramId());
    }

    @Test
    @DisplayName("Выброс исключения, если пользователь с таким Telegram ID уже зарегистрирован")
    void registerUser_WhenUserAlreadyExists_ShouldThrowUserAlreadyExistsException() {
        Long telegramId = 987654321L;
        String name = "ExistingCoder";
        String leetcodeUrl = "https://leetcode.com/existing";

        when(userRepository.existsByTelegramId(telegramId)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userRegistrationService.registerUser(telegramId, name, leetcodeUrl);
        }, "Ожидалось исключение UserAlreadyExistsException, но оно не было выброшено");

        verify(userRepository, never()).save(any(User.class));
    }
}
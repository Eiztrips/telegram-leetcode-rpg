package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.SubmissionMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.UserMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.SpringDataSubmissionRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.SpringDataUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock private SpringDataUserRepository userRepository;
    @Mock private SpringDataSubmissionRepository submissionRepository;
    @Mock private UserMapper userMapper;
    @Mock private SubmissionMapper submissionMapper;

    @InjectMocks private UserPersistenceAdapter userPersistenceAdapter;

    @Test
    void addSubmissions_WhenUserDoesNotExist_ShouldThrowException() {
        Long telegramId = 999L;
        List<SubmissionData> dtoList = List.of(mock(SubmissionData.class));
        when(userRepository.findById(telegramId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userPersistenceAdapter.addSubmissions(telegramId, dtoList))
                .isInstanceOf(UserExceptions.UserNotFoundException.class);

        verify(userRepository).findById(telegramId);
        verifyNoInteractions(submissionMapper);
    }

    @Test
    void addSubmissions_WhenUserExists_ShouldMapAndAddSubmissionsToEntity() {
        Long telegramId = 123L;
        List<SubmissionData> dtoList = List.of(mock(SubmissionData.class));

        UserEntity spyUserEntity = spy(UserEntity.builder()
                .telegramId(telegramId)
                .submissions(new ArrayList<>())
                .build());

        SubmissionEntity mockSubmissionEntity = mock(SubmissionEntity.class);
        List<SubmissionEntity> entityList = List.of(mockSubmissionEntity);

        when(userRepository.findById(telegramId)).thenReturn(Optional.of(spyUserEntity));
        when(submissionMapper.toEntityList(dtoList)).thenReturn(entityList);

        userPersistenceAdapter.addSubmissions(telegramId, dtoList);

        verify(spyUserEntity).addSubmissions(entityList);
        verify(userRepository).save(spyUserEntity);
    }
}
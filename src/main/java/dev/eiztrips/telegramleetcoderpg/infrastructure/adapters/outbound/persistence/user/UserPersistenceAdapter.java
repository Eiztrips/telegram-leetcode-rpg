package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.SubmissionMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.UserMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.SpringDataSubmissionRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component("userPersistenceAdapter")
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort {

	private final SpringDataUserRepository userRepository;
	private final SpringDataSubmissionRepository submissionRepository;
	private final UserMapper userMapper;
	private final SubmissionMapper submissionMapper;

	@Override
	@Transactional
	public void save(User user) {
		UserEntity entity = userRepository.findById(user.telegramId())
				.orElseGet(() -> UserEntity.builder().telegramId(user.telegramId()).build());

		userMapper.updateEntityFromDomain(user, entity);

		userRepository.save(entity);
	}

	@Override
	@Transactional
	public void addSubmissions(Long telegramId, List<SubmissionData> newSubmissionsData) {
		UserEntity userEntity = userRepository.findById(telegramId)
				.orElseThrow(() -> new UserExceptions.UserNotFoundException(telegramId));

		List<SubmissionEntity> submissionEntities = submissionMapper.toEntityList(newSubmissionsData);
		userEntity.addSubmissions(submissionEntities);

		userRepository.save(userEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> getByTelegramId(Long userTelegramId) {
		return userRepository.findById(userTelegramId).map(userMapper::toDomain);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubmissionData> getSubmissionsLastWeek(Long userTelegramId) {
		Instant oneWeekAgo = Instant.now().minus(Duration.ofDays(7));
		List<SubmissionEntity> submissionEntities = submissionRepository.findWeeklySubmissions(userTelegramId,
				oneWeekAgo);
		return submissionMapper.toDataListFromEntity(submissionEntities);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByTelegramId(Long userTelegramId) {
		return userRepository.existsById(userTelegramId);
	}
}

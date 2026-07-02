package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserCacheRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.dto.UserRegistrationCacheData;
import dev.eiztrips.telegramleetcoderpg.domain.exception.UserExceptions;
import dev.eiztrips.telegramleetcoderpg.domain.model.guild.Guild;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.user.UserRepositoryPort;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.entity.GuildEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.mapper.GuildMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.guild.repository.SpringDataGuildRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.SubmissionMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper.UserMapper;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.redis.RedisUserRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.spring.SpringDataSubmissionRepository;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.repository.spring.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component("userPersistenceAdapter")
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepositoryPort, UserCacheRepositoryPort {

	private final SpringDataUserRepository userRepository;
	private final SpringDataSubmissionRepository submissionRepository;
	private final SpringDataGuildRepository guildRepository;
	private final RedisUserRepository redisUserRepository;
	private final UserMapper userMapper;
	private final SubmissionMapper submissionMapper;
	private final GuildMapper guildMapper;

	@Override
	@Transactional
	public void save(User user) {
		UserEntity entity = userRepository.findById(user.telegramId())
				.orElseGet(() -> UserEntity.builder().telegramId(user.telegramId()).build());

		userMapper.updateEntityFromDomain(user, entity);

		if (user.guildId() != null) {
			GuildEntity guild = guildRepository.getReferenceById(user.guildId());
			entity.setGuild(guild);
		} else {
			entity.setGuild(null);
		}

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
	public Optional<User> getByLeetCodeUsername(String username) {
		return userRepository.findByLeetcodeUsername(username).map(userMapper::toDomain);
	}

	@Override
	public List<User> getUsersByGuildId(Long guildId) {
		return userMapper.toDomainList(userRepository.findAllByGuildId(guildId));
	}

	@Override
	public List<User> getUsersByGuildIdSortedByUserXpDesc(Long guildId) {
		return userMapper.toDomainList(userRepository.findAllByGuildIdOrderByXpDesc(guildId));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Guild> getGuildByUserTelegramId(Long userTelegramId) {
		return userRepository.findGuildByTelegramId(userTelegramId).map(guildMapper::toDomain);
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
	public void saveRegistrationToken(UserRegistrationCacheData data) {
		redisUserRepository.save(data);
	}

	@Override
	public Optional<UserRegistrationCacheData> getRegistrationCache(Long telegramChatId) {
		return redisUserRepository.findByChatId(telegramChatId);
	}

	@Override
	public void removeRegistrationCache(Long telegramChatId) {
		redisUserRepository.remove(telegramChatId);
	}
}

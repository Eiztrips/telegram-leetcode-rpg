package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper;

import dev.eiztrips.telegramleetcoderpg.domain.model.user.User;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

	@Mapping(target = "submissions", ignore = true)
	UserEntity toEntity(User user);
	List<UserEntity> toEntityList(List<User> users);

	@Mapping(target = "submissions", ignore = true)
	void updateEntityFromDomain(User user, @MappingTarget UserEntity entity);

	User toDomain(UserEntity entity);
	List<User> toDomainList(List<UserEntity> entities);
}

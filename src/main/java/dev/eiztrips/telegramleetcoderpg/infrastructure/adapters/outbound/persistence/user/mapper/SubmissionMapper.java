package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.dto.SubmissionData;
import dev.eiztrips.telegramleetcoderpg.domain.model.user.Submission;
import dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.entity.SubmissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubmissionMapper {

	@Mapping(target = "user", ignore = true)
	SubmissionEntity toEntity(SubmissionData data);
	List<SubmissionEntity> toEntityListFromDomain(List<Submission> domainList);

	Submission toDomain(SubmissionEntity entity);
	List<Submission> toDomainListFromEntity(List<SubmissionEntity> entities);

	SubmissionData toData(SubmissionEntity entity);
	List<SubmissionData> toDataListFromEntity(List<SubmissionEntity> entities);
}

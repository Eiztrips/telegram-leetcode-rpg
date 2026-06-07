package dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence.user.mapper;

import dev.eiztrips.telegramleetcoderpg.application.ports.outbound.a.shared.dto.SubmissionData;
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
	List<SubmissionEntity> toEntityList(List<SubmissionData> dataList);

	Submission toDomain(SubmissionEntity entity);
	List<Submission> toDomainListFromEntity(List<SubmissionEntity> entities);

	Submission toDomain(SubmissionData data);
	List<Submission> toDomainListFromData(List<SubmissionData> dataList);

	SubmissionData toData(SubmissionEntity entity);
	List<SubmissionData> toDataListFromEntity(List<SubmissionEntity> entities);
}

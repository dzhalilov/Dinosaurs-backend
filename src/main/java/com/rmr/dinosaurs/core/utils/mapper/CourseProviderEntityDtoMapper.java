package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.CourseProvider;
import com.rmr.dinosaurs.core.model.dto.ProviderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseProviderEntityDtoMapper {

  @Mapping(target = "shortDescription", ignore = true)
  @Mapping(target = "courses", ignore = true)
  CourseProvider toEntity(ProviderDto dto);

  ProviderDto toDto(CourseProvider entity);

}

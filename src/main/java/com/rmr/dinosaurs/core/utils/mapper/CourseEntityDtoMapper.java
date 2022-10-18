package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.dto.CreateCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseEntityDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "shortDescription", ignore = true)
  @Mapping(target = "internalRating", ignore = true)
  @Mapping(target = "isIndefinite", ignore = true)
  @Mapping(target = "isArchived", ignore = true)
  @Mapping(target = "provider", ignore = true)
  @Mapping(target = "courseAndProfessionRefs", ignore = true)
  @Mapping(target = "courseAndTagRefs", ignore = true)
  Course toEntity(CreateCourseDto dto);

  @Mapping(target = "providerId", source = "provider.id")
  CreateCourseDto toDto(Course entity);

}

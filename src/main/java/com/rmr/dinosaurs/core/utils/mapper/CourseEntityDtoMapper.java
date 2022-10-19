package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.dto.course.CreateUpdateCourseDto;
import com.rmr.dinosaurs.core.model.dto.course.ReadCourseDto;
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
  Course toEntity(CreateUpdateCourseDto dto);

  @Mapping(target = "professionId", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "providerId", source = "provider.id")
  CreateUpdateCourseDto toDto(Course entity);

  @Mapping(target = "professionId", ignore = true)
  @Mapping(target = "professionName", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "providerId", source = "provider.id")
  @Mapping(target = "providerName", source = "provider.name")
  @Mapping(target = "providerUrl", source = "provider.url")
  @Mapping(target = "providerCoverUrl", source = "provider.coverUrl")
  ReadCourseDto toReadCourseDto(Course entity);

}

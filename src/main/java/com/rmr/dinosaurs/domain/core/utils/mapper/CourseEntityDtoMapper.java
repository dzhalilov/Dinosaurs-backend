package com.rmr.dinosaurs.domain.core.utils.mapper;

import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.ShortCourseDto;
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
  @Mapping(target = "averageRating", ignore = true)
  @Mapping(target = "votes", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  Course toEntity(CourseCreateUpdateDto dto);

  @Mapping(target = "professionId", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "providerId", source = "provider.id")
  CourseCreateUpdateDto toDto(Course entity);

  @Mapping(target = "professionId", ignore = true)
  @Mapping(target = "professionName", ignore = true)
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "providerId", source = "provider.id")
  @Mapping(target = "providerName", source = "provider.name")
  @Mapping(target = "providerUrl", source = "provider.url")
  @Mapping(target = "providerCoverUrl", source = "provider.coverUrl")
  CourseReadDto toReadCourseDto(Course entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "title", ignore = true)
  @Mapping(target = "providerId", source = "provider.id")
  ShortCourseDto toShortCourseDto(Course entity);

}

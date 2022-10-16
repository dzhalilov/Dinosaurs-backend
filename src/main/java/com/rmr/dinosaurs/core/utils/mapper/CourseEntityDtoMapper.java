package com.rmr.dinosaurs.core.utils.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseEntityDtoMapper {

  Course toEntity(CreatingCourseDto dto);

}

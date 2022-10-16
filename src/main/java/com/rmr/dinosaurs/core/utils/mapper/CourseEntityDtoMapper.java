package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseEntityDtoMapper {

  Course toEntity(CreatingCourseDto dto);

  CourseDto toDto(Course entity);

}

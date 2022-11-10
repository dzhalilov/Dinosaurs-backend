package com.rmr.dinosaurs.domain.core.utils.mapper;

import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.core.model.CourseStudy;
import com.rmr.dinosaurs.domain.core.model.dto.course_study.CourseStudyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.course_study.CourseStudyResponseDto;
import com.rmr.dinosaurs.domain.userinfo.model.UserInfo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseStudyDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "endsAt", ignore = true)
  @Mapping(target = "score", ignore = true)
  @Mapping(target = "startsAt", source = "courseStudyCreateDto.startsAt")
  @Mapping(target = "userInfo", source = "userInfo")
  @Mapping(target = "course", source = "course")
  CourseStudy toEntity(CourseStudyCreateDto courseStudyCreateDto, UserInfo userInfo, Course course);

  @Mapping(target = "id", source = "courseStudy.id")
  @Mapping(target = "startsAt", source = "courseStudy.startsAt")
  @Mapping(target = "endsAt", source = "courseStudy.endsAt")
  @Mapping(target = "courseStudyInfoDto.courseId", source = "courseStudy.course.id")
  @Mapping(target = "courseStudyInfoDto.courseName", source = "courseStudy.course.title")
  @Mapping(target = "courseStudyInfoDto.courseProviderName",
      source = "courseStudy.course.provider.name")
  @Mapping(target = "courseStudyInfoDto.score", source = "courseStudy.score")
  @Mapping(target = "courseStudyInfoDto.professionNameSet",
      source = "professions")
  CourseStudyResponseDto toCourseStudyResponseDto(CourseStudy courseStudy,
      List<String> professions);
}

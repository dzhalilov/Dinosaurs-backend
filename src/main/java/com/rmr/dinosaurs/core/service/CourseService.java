package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCoursePageDto;
import java.util.List;

public interface CourseService {

  CourseDto createCourse(CourseDto course);

  ReadCourseDto getCourseById(long id);

  CourseDto updateCourseById(long id, CourseDto dto);

  List<ReadCourseDto> getAllCourses();

  ReadCoursePageDto getCoursePage(int pageNum);

}

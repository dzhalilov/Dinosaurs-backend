package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.core.model.dto.FilterParamsDto;
import java.util.List;

public interface CourseService {

  CourseCreateUpdateDto createCourse(CourseCreateUpdateDto course);

  CourseReadDto getCourseById(long id);

  CourseCreateUpdateDto editCourseById(long id, CourseCreateUpdateDto dto);

  List<CourseReadDto> getAllCourses();

  CourseReadPageDto getFilteredCoursePage(int pageNum, String sortBy, FilterParamsDto filter);

}

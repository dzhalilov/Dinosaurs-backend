package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CoursePageDto;
import com.rmr.dinosaurs.core.model.dto.CreateCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import java.util.List;

public interface CourseService {

  CreateCourseDto createCourse(CreateCourseDto course);

  ReadCourseDto getCourseById(long id);

  List<ReadCourseDto> getAllCourses();

  CoursePageDto getCoursePage(int pageNum);

}

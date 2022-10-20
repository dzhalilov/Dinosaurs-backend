package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.course.CreateUpdateCourseDto;
import com.rmr.dinosaurs.core.model.dto.course.ReadCourseDto;
import com.rmr.dinosaurs.core.model.dto.course.ReadCoursePageDto;
import java.util.List;

public interface CourseService {

  CreateUpdateCourseDto createCourse(CreateUpdateCourseDto course);

  ReadCourseDto getCourseById(long id);

  CreateUpdateCourseDto updateCourseById(long id, CreateUpdateCourseDto dto);

  List<ReadCourseDto> getAllCourses();

  ReadCoursePageDto getCoursePage(int pageNum);

}

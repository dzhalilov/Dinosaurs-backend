package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CreateCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;

public interface CourseService {

  CreateCourseDto createCourse(CreateCourseDto course);

  ReadCourseDto getCourseById(long id);

}

package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;

public interface CourseService {

  CourseDto addCourse(CreatingCourseDto course);

}

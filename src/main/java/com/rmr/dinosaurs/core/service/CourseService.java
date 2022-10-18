package com.rmr.dinosaurs.core.service;

import com.rmr.dinosaurs.core.model.Course;
import com.rmr.dinosaurs.core.model.dto.CreateCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CourseService {

  CreateCourseDto createCourse(CreateCourseDto course);

  ReadCourseDto getCourseById(long id);

  List<ReadCourseDto> getAllCourses();

  Page<Course> getCoursePage(int pageNum);

}

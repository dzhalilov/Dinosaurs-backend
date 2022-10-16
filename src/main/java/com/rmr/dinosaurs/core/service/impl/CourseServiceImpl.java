package com.rmr.dinosaurs.core.service.impl;

import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;
import com.rmr.dinosaurs.core.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

  @Override
  public CourseDto addCourse(CreatingCourseDto course) {
    return null;
  }
  
}

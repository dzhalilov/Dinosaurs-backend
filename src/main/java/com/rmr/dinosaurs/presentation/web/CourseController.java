package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.CreatingCourseDto;
import com.rmr.dinosaurs.core.service.CourseService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  public ResponseEntity addCourse(@RequestBody CreatingCourseDto course) {
    CourseDto createdCourse = courseService.addCourse(course);

    String createdUri = "/api/v1/courses/" + createdCourse.getId();
    return ResponseEntity.created(URI.create(createdUri)).build();
  }

}
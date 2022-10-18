package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.CreateCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import com.rmr.dinosaurs.core.service.CourseService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  public ResponseEntity<CreateCourseDto> createCourse(@RequestBody CreateCourseDto course) {
    CreateCourseDto createdCourse = courseService.createCourse(course);
    URI createdCourseUri = URI.create("/api/v1/courses/" + createdCourse.getId());
    return ResponseEntity
        .created(createdCourseUri)
        .body(createdCourse);
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<ReadCourseDto> getCourseById(@PathVariable long id) {
    ReadCourseDto course = courseService.getCourseById(id);
    return ResponseEntity
        .ok()
        .body(course);
  }

}

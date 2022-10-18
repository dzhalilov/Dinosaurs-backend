package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.model.dto.CourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCourseDto;
import com.rmr.dinosaurs.core.model.dto.ReadCoursePageDto;
import com.rmr.dinosaurs.core.service.CourseService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto course) {
    CourseDto createdCourse = courseService.createCourse(course);
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

  @PutMapping(path = "/{id}")
  public ResponseEntity<CourseDto> updateCourseById(
      @PathVariable long id, @RequestBody CourseDto dto) {

    CourseDto course = courseService.updateCourseById(id, dto);
    return ResponseEntity
        .ok()
        .body(course);
  }

  @GetMapping(path = "/all")
  public ResponseEntity<List<ReadCourseDto>> getAllProviders() {
    List<ReadCourseDto> courses = courseService.getAllCourses();
    return ResponseEntity
        .ok()
        .body(courses);
  }

  @GetMapping
  public ResponseEntity<ReadCoursePageDto> getCoursePage(@RequestParam(name = "page") int pageNum) {
    ReadCoursePageDto coursePage = courseService.getCoursePage(pageNum);
    return ResponseEntity
        .ok()
        .body(coursePage);
  }

}

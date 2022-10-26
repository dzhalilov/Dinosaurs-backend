package com.rmr.dinosaurs.presentation.web;

import com.rmr.dinosaurs.core.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.core.model.dto.FilterParamsDto;
import com.rmr.dinosaurs.core.service.CourseService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
  @ModeratorPermission
  public ResponseEntity<CourseCreateUpdateDto> createCourse(
      @RequestBody CourseCreateUpdateDto course) {

    CourseCreateUpdateDto createdCourse = courseService.createCourse(course);
    URI createdCourseUri = URI.create("/api/v1/courses/" + createdCourse.getId());
    return ResponseEntity
        .created(createdCourseUri)
        .body(createdCourse);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseReadDto> getCourseById(@PathVariable long id) {
    CourseReadDto course = courseService.getCourseById(id);
    return ResponseEntity
        .ok()
        .body(course);
  }

  @PutMapping("/{id}")
  @ModeratorPermission
  public ResponseEntity<CourseCreateUpdateDto> updateCourseById(
      @PathVariable long id, @RequestBody CourseCreateUpdateDto dto) {

    CourseCreateUpdateDto course = courseService.updateCourseById(id, dto);
    return ResponseEntity
        .ok()
        .body(course);
  }

  @GetMapping("/all")
  public ResponseEntity<List<CourseReadDto>> getAllCourses() {
    List<CourseReadDto> courses = courseService.getAllCourses();
    return ResponseEntity
        .ok()
        .body(courses);
  }

  @GetMapping
  public ResponseEntity<CourseReadPageDto> getFilteredCoursesPage(
      @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum,
      @RequestParam(name = "search", required = false) String search,
      @RequestParam(name = "isAdvanced", required = false) Boolean isAdvanced,
      @RequestParam(name = "professionId", required = false) Long professionId,
      @RequestParam(name = "startsAt", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startsAt,
      @RequestParam(name = "endsAt", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endsAt,
      @RequestParam(name = "sortBy", required = false) String sortBy) {

    FilterParamsDto filter = new FilterParamsDto(
        search, isAdvanced, professionId, startsAt, endsAt
    );

    CourseReadPageDto coursePage = courseService.getFilteredCoursePage(pageNum, sortBy, filter);
    return ResponseEntity
        .ok()
        .body(coursePage);
  }

}

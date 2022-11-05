package com.rmr.dinosaurs.presentation.web.core;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.*;
import com.rmr.dinosaurs.domain.core.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

  private final CourseService courseService;

  @Operation(description = "create course profile data using dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created course data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseCreateUpdateDto.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to create provided course",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping
  @ModeratorPermission
  public ResponseEntity<CourseCreateUpdateDto> addCourse(
      @RequestBody @Valid CourseCreateUpdateDto course) {

    CourseCreateUpdateDto createdCourse = courseService.addCourse(course);
    URI createdCourseUri = URI.create("/api/v1/courses/" + createdCourse.getId());
    return ResponseEntity
        .created(createdCourseUri)
        .body(createdCourse);
  }

  @Operation(description = "get course profile data by its id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got course profile by id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseReadDto.class))}),
      @ApiResponse(responseCode = "404", description = "course profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/{courseId}")
  public ResponseEntity<CourseReadDto> getCourseById(@PathVariable long courseId) {
    CourseReadDto course = courseService.getCourseById(courseId);
    return ResponseEntity
        .ok()
        .body(course);
  }

  @Operation(description = "edit course profile data using its id and dto")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got edited course data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseCreateUpdateDto.class))}),
      @ApiResponse(responseCode = "404", description = "course profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "profession profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user has no permissions to edit provided course",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PutMapping("/{courseId}")
  @ModeratorPermission
  public ResponseEntity<CourseCreateUpdateDto> editCourseById(
      @PathVariable long courseId, @RequestBody CourseCreateUpdateDto courseDto) {

    CourseCreateUpdateDto course = courseService.editCourseById(courseId, courseDto);
    return ResponseEntity
        .ok()
        .body(course);
  }

  @Operation(description = "get all course profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got list of course profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseReadDto.class))})})
  @GetMapping("/all")
  public ResponseEntity<List<CourseReadDto>> getAllCourses() {
    List<CourseReadDto> courses = courseService.getAllCourses();
    return ResponseEntity
        .ok()
        .body(courses);
  }

  @Operation(description = "get page of filtered course profiles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got page of course profiles",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseReadPageDto.class))}),
      @ApiResponse(responseCode = "400", description = "not positive page number",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
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
    log.info("Get all courses with filter={}", filter);
    CourseReadPageDto coursePage = courseService.getFilteredCoursePage(pageNum, sortBy, filter);
    return ResponseEntity
        .ok()
        .body(coursePage);
  }

  @Operation(description = "create course review")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created course review data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ReviewDto.class))}),
      @ApiResponse(responseCode = "404", description = "course not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400",
          description = "current user already made review for this course id",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/{courseId}/review")
  public ResponseEntity<ReviewDto> addCourseReview(
      @PathVariable Long courseId,
      @RequestBody @Valid ReviewDto reviewDto,
      Principal principal) {
    String email = principal.getName();
    log.info("Make review for course id={} by user={}", courseId, email);
    ReviewDto createdReview = courseService.addReview(courseId, reviewDto, principal);
    URI createdCourseUri = URI.create("/api/v1/courses/" + courseId + "/review" + createdReview.getId());
    return ResponseEntity
        .created(createdCourseUri)
        .body(createdReview);
  }

}

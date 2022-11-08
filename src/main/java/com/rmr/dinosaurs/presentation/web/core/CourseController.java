package com.rmr.dinosaurs.presentation.web.core;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseStudyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseStudyResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.FilterParamsDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import com.rmr.dinosaurs.domain.core.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "Course controller")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

  private final CourseService courseService;

  @Operation(summary = "Create course profile data using dto")
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

  @Operation(summary = "Get course profile data by its id")
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

  @Operation(summary = "Edit course profile data using its id and dto")
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

  @Operation(summary = "Get all course profiles")
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

  @Operation(summary = "Get page of filtered course profiles")
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

  @Operation(summary = "Create course review")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "got created course review data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ReviewResponseDto.class))}),
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
  @PostMapping("/{courseId}/reviews")
  public ResponseEntity<ReviewResponseDto> addCourseReview(
      @PathVariable Long courseId,
      @RequestBody @Valid ReviewCreateDto reviewCreateDto,
      Principal principal) {
    String email = principal.getName();
    log.info("Make review for course id={} by user={}", courseId, email);
    ReviewResponseDto createdReview = courseService.addReview(courseId, reviewCreateDto, principal);
    URI createdCourseUri = URI.create(
        "/api/v1/courses/" + courseId + "/review" + createdReview.getId());
    return ResponseEntity
        .created(createdCourseUri)
        .body(createdReview);
  }

  @Operation(summary = "Get all reviews for current course")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "got course review data",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ReviewResponseDto.class))}),
      @ApiResponse(responseCode = "404", description = "course not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @GetMapping("/{courseId}/reviews")
  public ResponseEntity<List<ReviewResponseDto>> addCourseReview(@PathVariable Long courseId) {
    log.info("Get all reviews for course id={}", courseId);
    List<ReviewResponseDto> reviewDtoList = courseService.getReviewsByCourseId(courseId);
    return ResponseEntity.ok().body(reviewDtoList);
  }

  @Operation(summary = "Create of starting study course")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "created course study information",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ReviewResponseDto.class))}),
      @ApiResponse(responseCode = "404", description = "course not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "404", description = "provider profile not found",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))}),
      @ApiResponse(responseCode = "400", description = "bad request",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/{courseId}/start-study")
  public ResponseEntity<CourseStudyResponseDto> startCourseStudy(@PathVariable Long courseId,
      Principal principal, @RequestBody @Valid CourseStudyCreateDto courseStudyCreateDto) {
    String email = principal.getName();
    log.info("Created course study info for user={} and course id={}", email, courseId);
    CourseStudyResponseDto courseStudyResponseDto = courseService.createCourseStudy(principal, courseId,
        courseStudyCreateDto);
    URI CourseStudyUri = URI.create(
        "/api/v1/profiles/study_info" + courseStudyResponseDto.getId());
    return ResponseEntity.created(CourseStudyUri).body(courseStudyResponseDto);
  }

}

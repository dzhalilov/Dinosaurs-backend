package com.rmr.dinosaurs.presentation.web.statistics;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionSearchCriteria;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionsUniqueSearchCriteria;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionsUniqueStatisticsDto;
import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics controller")
@RequiredArgsConstructor
public class StatisticsController {

  private final CourseStatisticsService courseStatisticsService;


  @Operation(summary = "register course link transition")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "write course link transition to stats",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = void.class))}),
      @ApiResponse(responseCode = "400", description = "wrong request format",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/course/{courseId}")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerCourseTransition(@PathVariable Long courseId) {
    courseStatisticsService.courseTransitionRegister(courseId);
  }

  @Operation(summary = "Get filtered list of course link transition dto as page")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "filtered page of course link transitions",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseLinkTransitionPageDto.class))}),
      @ApiResponse(responseCode = "400", description = "wrong request format",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/course/search")
  @ModeratorPermission
  public CourseLinkTransitionPageDto getAllCourseLinkTransitionsByPagesAndFilters(
      @RequestBody @Valid CourseLinkTransitionFilterDto courseLinkTransitionFilterDto) {
    return courseStatisticsService.getAllCourseLinkTransitionsByFilter(
        courseLinkTransitionFilterDto);
  }

  @Operation(summary = "Get filtered list of unique course link transitions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "filtered list of unique course link transitions",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = CourseLinkTransitionsUniqueStatisticsDto.class))}),
      @ApiResponse(responseCode = "400", description = "wrong request format",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/course/search/unique")
  @ModeratorPermission
  public List<CourseLinkTransitionsUniqueStatisticsDto> getUniqueCourseLinkTransitionsStats(
      @RequestBody @Valid CourseLinkTransitionsUniqueSearchCriteria searchCriteria) {
    return courseStatisticsService.getCourseLinkTransitionsUniqueStatisticsByFilter(
        searchCriteria);
  }

  @Operation(summary = "Get filtered list of course link transition dto exported to excel")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "filtered excel of course link transitions",
          content = {@Content(mediaType = "application/octet-stream",
              schema = @Schema(implementation = void.class))}),
      @ApiResponse(responseCode = "400", description = "wrong request format or export error",
          content = {@Content(mediaType = "application/json",
              schema = @Schema(implementation = ServiceException.class))})})
  @PostMapping("/course/search/export/excel")
  @ModeratorPermission
  public void exportCourseStatistics(
      @RequestBody @Valid CourseLinkTransitionSearchCriteria cltSearchCriteria,
      HttpServletResponse response) {
    courseStatisticsService.getFilteredCourseLinkTransitionsAsXlsx(
        cltSearchCriteria, response);
  }

}

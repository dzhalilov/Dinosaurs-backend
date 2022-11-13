package com.rmr.dinosaurs.presentation.web.statistics;

import com.rmr.dinosaurs.domain.auth.security.permission.ModeratorPermission;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;
import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsService;
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
@RequiredArgsConstructor
public class StatisticsController {

  private final CourseStatisticsService courseStatisticsService;

  @PostMapping("/course/{courseId}")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerCourseTransition(@PathVariable Long courseId) {
    courseStatisticsService.courseTransitionRegister(courseId);
  }

  @PostMapping("/course/search")
  @ModeratorPermission
  public CourseLinkTransitionPageDto getAllCourseLinkTransitionsByPagesAndFilters(
      @RequestBody @Valid
      CourseLinkTransitionFilterDto courseLinkTransitionFilterDto) {
    return courseStatisticsService.getAllCourseLinkTransitionsByFilter(
        courseLinkTransitionFilterDto);
  }

}

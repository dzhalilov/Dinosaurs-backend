package com.rmr.dinosaurs.presentation.web.statistics;

import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

  private final CourseStatisticsService courseStatisticsService;

  @PostMapping("/course/{courseId}")
  public void registerCourseTransition(@RequestParam Long courseId) {
    courseStatisticsService.courseTransitionRegister(courseId);
  }

}

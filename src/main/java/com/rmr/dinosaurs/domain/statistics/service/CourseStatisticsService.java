package com.rmr.dinosaurs.domain.statistics.service;

import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;

public interface CourseStatisticsService {

  /**
   * register course link transition
   *
   * @param courseId course id
   */
  void courseTransitionRegister(Long courseId);

  /**
   * get all course link transitions by filter
   *
   * @param courseLinkTransitionFilterDto search criteria
   * @return course link transition page
   */
  CourseLinkTransitionPageDto getAllCourseLinkTransitionsByFilter(
      CourseLinkTransitionFilterDto courseLinkTransitionFilterDto);

}

package com.rmr.dinosaurs.domain.statistics.service;

import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionSearchCriteria;
import javax.servlet.http.HttpServletResponse;

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

  /**
   * get all course link transitions by filter as Excel file
   *
   * @param searchCriteria search course link transitions criteria
   */
  void getFilteredCourseLinkTransitionsAsXlsx(
      CourseLinkTransitionSearchCriteria searchCriteria,
      HttpServletResponse response);

}

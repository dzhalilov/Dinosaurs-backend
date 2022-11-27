package com.rmr.dinosaurs.domain.statistics.service;

import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionSearchCriteria;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionsUniqueSearchCriteria;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionsUniqueStatisticsDto;
import java.util.List;
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
   * get unique pairs of course id and link transitions by filter
   *
   * @param searchCriteria search criteria
   * @return unique pairs of course id and link transitions
   */
  List<CourseLinkTransitionsUniqueStatisticsDto> getUniqueTransitionsStatisticsByFilter(
      CourseLinkTransitionsUniqueSearchCriteria searchCriteria);

  /**
   * get all course link transitions by filter as Excel file
   *
   * @param searchCriteria search course link transitions criteria
   */
  void getFilteredCourseLinkTransitionsAsXlsx(
      CourseLinkTransitionSearchCriteria searchCriteria,
      HttpServletResponse response);

}

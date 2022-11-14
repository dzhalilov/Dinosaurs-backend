package com.rmr.dinosaurs.domain.statistics.service;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface CourseStatisticsExporterService {

  void exportToExcel(List<CourseLinkTransition> courseLinkTransitions,
      HttpServletResponse response);

}

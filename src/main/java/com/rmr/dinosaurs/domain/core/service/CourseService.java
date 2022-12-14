package com.rmr.dinosaurs.domain.core.service;

import com.rmr.dinosaurs.domain.core.model.dto.CourseCreateUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadDto;
import com.rmr.dinosaurs.domain.core.model.dto.CourseReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.FilterParamsDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.ReviewResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.ShortCourseDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyCreateDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyReadPageDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyResponseDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.CourseStudyUpdateDto;
import com.rmr.dinosaurs.domain.core.model.dto.study.FilterCourseStudyParamsDto;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public interface CourseService {

  /**
   * create course profile data
   *
   * @param courseDto to be created
   * @return created course profile data
   */
  CourseCreateUpdateDto addCourse(CourseCreateUpdateDto courseDto);

  /**
   * get course profile data by course id
   *
   * @param courseId course profiler id
   * @return course profile
   */
  CourseReadDto getCourseById(long courseId);

  /**
   * set course profile data
   *
   * @param courseId  course profile id to be changed to
   * @param courseDto to be changed to
   * @return changed course profile data
   */
  CourseCreateUpdateDto editCourseById(long courseId, CourseCreateUpdateDto courseDto);

  /**
   * get list of all course profiles
   *
   * @return list of course profile data
   */
  List<CourseReadDto> getAllCourses();

  /**
   * get page of filtered course profiles
   *
   * @param pageNum page number
   * @param sortBy  sort by startsAt or endsAt param
   * @param filter  filter params
   * @return page of filtered course profile data
   */
  CourseReadPageDto getFilteredCoursePage(int pageNum, String sortBy, FilterParamsDto filter);

  ReviewResponseDto addReview(Long courseId, ReviewCreateDto reviewDto, Principal principal);

  List<ReviewResponseDto> getReviewsByCourseId(Long courseId);

  CourseStudyResponseDto createCourseStudy(Principal principal, Long courseId,
      CourseStudyCreateDto courseStudyCreateDto);

  List<CourseStudyResponseDto> getMyCourseStudy(Principal principal);

  void finishCourseStudy(Long courseId, CourseStudyUpdateDto courseStudyUpdateDto);

  CourseStudyReadPageDto getFilteredCourseInformationPage(int pageNum,
      FilterCourseStudyParamsDto filter);

  void exportFilteredCourseInformationToPdf(FilterCourseStudyParamsDto filter,
      HttpServletResponse response);

  /**
   * get list of courses in a short representation by provider id
   *
   * @param providerId id of the course provider
   * @return list of short courses dtos
   */
  List<ShortCourseDto> getAllCoursesByProviderId(Long providerId);

}

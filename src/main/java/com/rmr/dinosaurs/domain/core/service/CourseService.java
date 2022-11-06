package com.rmr.dinosaurs.domain.core.service;

import com.rmr.dinosaurs.domain.core.model.dto.*;

import java.security.Principal;
import java.util.List;

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

}

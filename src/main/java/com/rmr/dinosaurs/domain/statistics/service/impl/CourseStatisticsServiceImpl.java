package com.rmr.dinosaurs.domain.statistics.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseErrorCode.COURSE_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.statistics.configuration.properties.CourseStatisticsProperties;
import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionFilterDto;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionPageDto;
import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsService;
import com.rmr.dinosaurs.domain.statistics.utils.converter.CourseLinkTransitionConverter;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.statistics.CourseLinkTransitionRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseStatisticsServiceImpl implements CourseStatisticsService {

  private final CourseStatisticsProperties properties;
  private final CourseLinkTransitionConverter courseLinkTransitionConverter;

  private final CourseLinkTransitionRepository courseLinkTransitionRepository;
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;


  @Override
  public void courseTransitionRegister(Long courseId) {
    log.info("Link transition to course with id: {}", courseId);
    getCurrentUserPrincipal().ifPresent(userPrincipal -> {
          var course = courseRepository.findById(courseId).orElseThrow(
              () -> new ServiceException(COURSE_NOT_FOUND));
          var user = userRepository.findById(userPrincipal.getId()).orElseThrow(
              () -> new ServiceException(USER_NOT_FOUND));
          courseLinkTransitionRepository.save(
              new CourseLinkTransition(null, course, user,
                  LocalDateTime.now(ZoneOffset.UTC))
          );
        }
    );
  }

  @Override
  public CourseLinkTransitionPageDto getAllCourseLinkTransitionsByFilter(
      CourseLinkTransitionFilterDto filter) {
    var page = PageRequest.of(
        filter.getPage(),
        Objects.nonNull(filter.getPageSize())
            ? filter.getPageSize()
            : properties.getDefaultPageSize()
    );
    var courseLinkTransitionPage = courseLinkTransitionRepository.findAllByFilter(
        filter.getCoursesIds(), filter.getUserEmail(),
        filter.getTransitionedFrom(), filter.getTransitionedTo(), page);
    var courseLinkTransitionDtos = courseLinkTransitionPage.stream()
        .map(courseLinkTransitionConverter::toCourseLinkTransitionDto)
        .toList();
    return new CourseLinkTransitionPageDto(courseLinkTransitionPage.getTotalElements(),
        courseLinkTransitionPage.getTotalPages(),
        courseLinkTransitionPage.getSize(),
        courseLinkTransitionPage.getNumber(),
        courseLinkTransitionDtos);
  }

  private Optional<DinoPrincipal> getCurrentUserPrincipal() {
    return Optional.of((DinoPrincipal) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal());
  }

}

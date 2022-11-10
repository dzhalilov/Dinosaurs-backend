package com.rmr.dinosaurs.domain.statistics.service.impl;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.UserErrorCode.USER_NOT_FOUND;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.CourseErrorCode.COURSE_NOT_FOUND;

import com.rmr.dinosaurs.domain.auth.model.User;
import com.rmr.dinosaurs.domain.auth.security.model.DinoPrincipal;
import com.rmr.dinosaurs.domain.core.exception.ServiceException;
import com.rmr.dinosaurs.domain.core.model.Course;
import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.service.CourseStatisticsService;
import com.rmr.dinosaurs.infrastucture.database.auth.UserRepository;
import com.rmr.dinosaurs.infrastucture.database.core.CourseRepository;
import com.rmr.dinosaurs.infrastucture.database.statistics.CourseLinkTransitionRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseStatisticsServiceImpl implements CourseStatisticsService {

  private final CourseLinkTransitionRepository courseLinkTransitionRepository;
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  @Override
  public void courseTransitionRegister(Long courseId) {
    getCurrentUserPrincipal().ifPresent(userPrincipal -> {
          Course course = courseRepository.findById(courseId).orElseThrow(
              () -> new ServiceException(COURSE_NOT_FOUND));
          User user = userRepository.findById(userPrincipal.getId()).orElseThrow(
              () -> new ServiceException(USER_NOT_FOUND));
          courseLinkTransitionRepository.save(
              new CourseLinkTransition(null, course, user, LocalDateTime.now(ZoneOffset.UTC))
          );
        }
    );
  }

  private Optional<DinoPrincipal> getCurrentUserPrincipal() {
    return Optional.of((DinoPrincipal) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal());
  }

}

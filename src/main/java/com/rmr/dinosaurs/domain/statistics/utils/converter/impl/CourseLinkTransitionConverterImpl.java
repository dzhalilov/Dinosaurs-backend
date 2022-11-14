package com.rmr.dinosaurs.domain.statistics.utils.converter.impl;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionDto;
import com.rmr.dinosaurs.domain.statistics.utils.converter.CourseLinkTransitionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseLinkTransitionConverterImpl implements CourseLinkTransitionConverter {

  @Override
  public CourseLinkTransitionDto toCourseLinkTransitionDto(
      CourseLinkTransition courseLinkTransition) {
    return new CourseLinkTransitionDto(
        courseLinkTransition.getId(),
        courseLinkTransition.getUser().getEmail(),
        courseLinkTransition.getCourse().getId(),
        courseLinkTransition.getCourse().getTitle(),
        courseLinkTransition.getTransitionedAt()
    );
  }

}

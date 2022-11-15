package com.rmr.dinosaurs.domain.statistics.utils.converter;

import com.rmr.dinosaurs.domain.statistics.model.CourseLinkTransition;
import com.rmr.dinosaurs.domain.statistics.model.dto.CourseLinkTransitionDto;

public interface CourseLinkTransitionConverter {

  CourseLinkTransitionDto toCourseLinkTransitionDto(CourseLinkTransition courseLinkTransition);

}

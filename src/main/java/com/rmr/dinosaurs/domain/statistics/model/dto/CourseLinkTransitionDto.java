package com.rmr.dinosaurs.domain.statistics.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseLinkTransitionDto {

  UUID id;

  String userEmail;

  String courseName;

  String courseProvider;

  LocalDateTime transitionedAt;

}

package com.rmr.dinosaurs.domain.core.model.dto.study;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterCourseStudyParamsDto {

  String courseTitle;
  String profession;
  Long score;
  LocalDateTime endsAt;
  Boolean isFinished;
}
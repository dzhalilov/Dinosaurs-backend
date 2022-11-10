package com.rmr.dinosaurs.domain.core.model.dto.study;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudyResponseDto {

  private Long id;
  private LocalDateTime startsAt;
  private LocalDateTime endsAt;
  private CourseStudyInfoDto courseStudyInfoDto;

}

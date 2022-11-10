package com.rmr.dinosaurs.domain.core.model.dto.course_study;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudyInfoResponseDto {

  private String userInfoNameAndSurname;
  private String email;
  private String courseTitle;
  private boolean isCourseFinished;
  private LocalDateTime finishedAt;
  private Long score;
  private Set<String> professions;

}

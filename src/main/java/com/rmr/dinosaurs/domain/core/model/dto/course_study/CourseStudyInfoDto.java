package com.rmr.dinosaurs.domain.core.model.dto.course_study;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudyInfoDto {

  private Long courseId;
  private String courseName;
  private String courseProviderName;
  private Long score;
  private Set<String> professionNameSet;

}

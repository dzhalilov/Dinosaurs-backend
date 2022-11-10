package com.rmr.dinosaurs.domain.core.model.dto.study;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStudyReadPageDto {

  Long totalElements;
  Integer totalPages;
  Integer pageSize;
  Integer pageNumber;
  List<CourseStudyInfoResponseDto> content;
}
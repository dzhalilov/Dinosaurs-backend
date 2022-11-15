package com.rmr.dinosaurs.domain.statistics.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseLinkTransitionPageDto {

  Long totalElements;

  Integer totalPages;

  Integer pageSize;

  Integer pageNumber;

  List<CourseLinkTransitionDto> courseLinkTransitionDtos;

}

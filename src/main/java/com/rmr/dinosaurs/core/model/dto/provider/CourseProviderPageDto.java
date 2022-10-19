package com.rmr.dinosaurs.core.model.dto.provider;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProviderPageDto {

  Long totalElements;

  Integer totalPages;

  Integer pageSize;

  Integer pageNumber;

  List<CourseProviderDto> content;

}

package com.rmr.dinosaurs.core.model.dto;

import com.rmr.dinosaurs.core.model.Course;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePageDto {

  Long totalElements;

  Integer totalPages;

  Integer pageSize;

  Integer pageNumber;

  List<Course> content;

}

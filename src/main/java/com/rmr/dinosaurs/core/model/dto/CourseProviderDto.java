package com.rmr.dinosaurs.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProviderDto {

  Long id;

  String name;

  String url;

  String description;

  String coverUrl;

}

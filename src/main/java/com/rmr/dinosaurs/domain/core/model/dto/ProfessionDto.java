package com.rmr.dinosaurs.domain.core.model.dto;

import com.rmr.dinosaurs.domain.core.utils.validator.UrlConstraintValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDto {

  Long id;

  String name;

  @UrlConstraintValidator
  String coverUrl;

  String description;

}

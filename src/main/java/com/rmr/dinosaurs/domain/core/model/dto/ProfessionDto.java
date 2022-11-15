package com.rmr.dinosaurs.domain.core.model.dto;

import com.rmr.dinosaurs.domain.core.utils.validator.UrlConstraintValidator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDto {

  Long id;

  @NotNull
  @NotEmpty
  @Size(min = 2, max = 255)
  String name;

  @UrlConstraintValidator
  String coverUrl;

  @NotEmpty
  @NotNull
  @Size(min = 20, max = 1000)
  String description;

}

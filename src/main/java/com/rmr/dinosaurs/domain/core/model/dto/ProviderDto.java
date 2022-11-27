package com.rmr.dinosaurs.domain.core.model.dto;

import com.rmr.dinosaurs.domain.core.utils.validator.UrlConstraintValidator;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDto {

  Long id;

  @Length(min = 2, max = 255)
  @NotNull
  String name;

  @UrlConstraintValidator
  String url;

  @Length(min = 2, max = 1000)
  @NotNull
  String description;

  @UrlConstraintValidator
  String coverUrl;

}

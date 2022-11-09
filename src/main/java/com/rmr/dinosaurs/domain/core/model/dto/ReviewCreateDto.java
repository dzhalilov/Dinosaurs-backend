package com.rmr.dinosaurs.domain.core.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {

  @Min(1)
  @Max(5)
  @NotNull
  Integer rating;
  @Length(max = 1000)
  String textReview;
}

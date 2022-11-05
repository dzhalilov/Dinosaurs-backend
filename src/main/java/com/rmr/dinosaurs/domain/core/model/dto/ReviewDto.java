package com.rmr.dinosaurs.domain.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
  Long id;
  @Min(1)
  @Max(5)
  Integer rating;
  @Length(max = 1000)
  String textReview;
}

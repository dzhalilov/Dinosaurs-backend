package com.rmr.dinosaurs.domain.core.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

  Long id;
  LocalDateTime created;
  Integer rating;
  String textReview;
  String userInfoName;
  String userInfoSurname;
}

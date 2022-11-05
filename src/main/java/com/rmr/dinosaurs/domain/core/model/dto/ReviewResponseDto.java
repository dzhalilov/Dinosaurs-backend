package com.rmr.dinosaurs.domain.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

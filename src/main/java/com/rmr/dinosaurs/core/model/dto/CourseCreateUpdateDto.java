package com.rmr.dinosaurs.core.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseCreateUpdateDto {

  Long id;

  String title;

  String url;

  String coverUrl;

  String description;

  LocalDateTime startsAt;

  LocalDateTime endsAt;

  Boolean isAdvanced;

  Long providerId;

  Long professionId;

  String[] tags;

}

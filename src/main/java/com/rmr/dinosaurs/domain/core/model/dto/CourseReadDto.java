package com.rmr.dinosaurs.domain.core.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseReadDto {

  Long id;

  String title;

  String url;

  String coverUrl;

  String description;

  LocalDateTime startsAt;

  LocalDateTime endsAt;

  Boolean isAdvanced;

  Long providerId;

  String providerName;

  String providerUrl;

  String providerCoverUrl;

  Long professionId;

  String professionName;

  Double averageRating;

  Long votes;

  List<String> tags;

}

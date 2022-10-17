package com.rmr.dinosaurs.core.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatingCourseDto {

  String title;
  String url;
  String coverUrl;
  String description;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  LocalDateTime startsAt;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  LocalDateTime endsAt;
  Boolean isAdvanced;
  String providerUrl;
  String profession;
  String[] tags;

}

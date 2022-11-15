package com.rmr.dinosaurs.domain.statistics.model.dto;

import java.time.LocalDateTime;
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseLinkTransitionSearchCriteria {

  @Nullable
  Set<Long> coursesIds;

  @Nullable
  String userEmail;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  LocalDateTime transitionedFrom;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  LocalDateTime transitionedTo;

}

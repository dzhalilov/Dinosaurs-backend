package com.rmr.dinosaurs.core.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterParamsDto {

  String search;

  Boolean isAdvanced;

  Long professionId;

  LocalDateTime startsAt;

  LocalDateTime endsAt;

}

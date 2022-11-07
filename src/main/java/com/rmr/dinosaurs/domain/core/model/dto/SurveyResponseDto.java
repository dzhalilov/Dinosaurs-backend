package com.rmr.dinosaurs.domain.core.model.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponseDto {

  @NotNull
  Long surveyId;

  @NotNull
  @NotEmpty
  List<SurveyResponseQuestionDto> survey;

}

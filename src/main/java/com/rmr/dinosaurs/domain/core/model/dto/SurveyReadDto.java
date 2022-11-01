package com.rmr.dinosaurs.domain.core.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyReadDto {

  Long surveyId;

  String title;

  String description;

  List<QuestionReadDto> survey;

}

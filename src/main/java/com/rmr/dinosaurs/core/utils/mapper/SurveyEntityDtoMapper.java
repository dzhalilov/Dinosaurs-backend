package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Survey;
import com.rmr.dinosaurs.core.model.SurveyQuestion;
import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.core.model.dto.survey.AnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.QuestionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SurveyEntityDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "shortDescription", ignore = true)
  @Mapping(target = "questions", ignore = true)
  Survey toSurvey(CreateSurveyDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "survey", ignore = true)
  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "text", source = "question")
  SurveyQuestion toSurveyQuestion(QuestionDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "question", ignore = true)
  @Mapping(target = "profession", ignore = true)
  SurveyQuestionAnswer toSurveyQuestionAnswer(AnswerDto dto);

}

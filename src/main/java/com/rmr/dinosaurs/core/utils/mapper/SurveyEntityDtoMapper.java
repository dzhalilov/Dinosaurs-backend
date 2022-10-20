package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Survey;
import com.rmr.dinosaurs.core.model.SurveyQuestion;
import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.core.model.dto.survey.CreateAnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateQuestionDto;
import com.rmr.dinosaurs.core.model.dto.survey.CreateSurveyDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadAnswerDto;
import com.rmr.dinosaurs.core.model.dto.survey.ReadQuestionDto;
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
  SurveyQuestion toSurveyQuestion(CreateQuestionDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "question", ignore = true)
  @Mapping(target = "profession", ignore = true)
  SurveyQuestionAnswer toSurveyQuestionAnswer(CreateAnswerDto dto);

  @Mapping(target = "answerId", source = "id")
  ReadAnswerDto toReadAnswerDto(SurveyQuestionAnswer entity);

  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "questionId", source = "id")
  @Mapping(target = "question", source = "text")
  ReadQuestionDto toReadQuestionDto(SurveyQuestion entity);

}

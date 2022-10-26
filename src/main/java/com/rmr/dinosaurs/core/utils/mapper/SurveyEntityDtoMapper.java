package com.rmr.dinosaurs.core.utils.mapper;

import com.rmr.dinosaurs.core.model.Survey;
import com.rmr.dinosaurs.core.model.SurveyQuestion;
import com.rmr.dinosaurs.core.model.SurveyQuestionAnswer;
import com.rmr.dinosaurs.core.model.dto.AnswerCreateDto;
import com.rmr.dinosaurs.core.model.dto.AnswerReadDto;
import com.rmr.dinosaurs.core.model.dto.QuestionCreateDto;
import com.rmr.dinosaurs.core.model.dto.SurveyCreateDto;
import com.rmr.dinosaurs.core.model.dto.QuestionReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SurveyEntityDtoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "shortDescription", ignore = true)
  @Mapping(target = "questions", ignore = true)
  Survey toSurvey(SurveyCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "survey", ignore = true)
  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "text", source = "question")
  SurveyQuestion toSurveyQuestion(QuestionCreateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "question", ignore = true)
  @Mapping(target = "profession", ignore = true)
  SurveyQuestionAnswer toSurveyQuestionAnswer(AnswerCreateDto dto);

  @Mapping(target = "answerId", source = "id")
  AnswerReadDto toReadAnswerDto(SurveyQuestionAnswer entity);

  @Mapping(target = "answers", ignore = true)
  @Mapping(target = "questionId", source = "id")
  @Mapping(target = "question", source = "text")
  QuestionReadDto toReadQuestionDto(SurveyQuestion entity);

}

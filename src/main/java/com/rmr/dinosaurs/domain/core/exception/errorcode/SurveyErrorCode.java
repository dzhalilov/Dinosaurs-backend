package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SurveyErrorCode implements ErrorCode {

  SURVEY_NOT_FOUND("ITD_SEC_1", "No such survey found", HttpStatus.NOT_FOUND),
  SURVEY_QUESTION_ANSWER_WITH_NO_PROFESSION_ID(
      "ITD_SEC_2", "Survey question answer with no profession id", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

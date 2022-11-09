package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseErrorCode implements ErrorCode {

  COURSE_NOT_FOUND("ITD_CEC_1", "No such course found", HttpStatus.NOT_FOUND),
  COURSE_END_DATE_BEFORE_OR_EQUALS_START_DATE("ITD_CEC_2",
      "Course end date have to be after start date", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

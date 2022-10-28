package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseErrorCode implements ErrorCode {

  COURSE_NOT_FOUND("ITD_CEC_1", "No such course found", HttpStatus.NOT_FOUND);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

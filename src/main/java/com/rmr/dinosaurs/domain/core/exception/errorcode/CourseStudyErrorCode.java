package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseStudyErrorCode implements ErrorCode {

  DUPLICATE_STUDY_ERROR("ITD_CSE_1", "Duplicate course study creation error",
      HttpStatus.BAD_REQUEST),
  USER_ROLE_ERROR("ITD_CSE_2", "Just regular users can study", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;
}

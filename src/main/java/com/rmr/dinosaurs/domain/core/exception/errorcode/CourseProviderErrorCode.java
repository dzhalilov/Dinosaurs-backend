package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseProviderErrorCode implements ErrorCode {

  COURSE_PROVIDER_NOT_FOUND("ITD_CPEC_1", "No such course provider found", HttpStatus.NOT_FOUND);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

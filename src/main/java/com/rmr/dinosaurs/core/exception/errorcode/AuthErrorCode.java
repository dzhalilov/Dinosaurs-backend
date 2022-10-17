package com.rmr.dinosaurs.core.exception.errorcode;

import com.rmr.dinosaurs.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

  INCORRECT_CREDENTIALS("ITD_AEC_1", "Incorrect credentials provided", HttpStatus.BAD_REQUEST),
  INVALID_TOKEN_PROVIDED("ITD_AEC_2", "Invalid token provided", HttpStatus.FORBIDDEN);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

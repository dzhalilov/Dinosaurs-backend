package com.rmr.dinosaurs.domain.auth.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

  USER_NOT_FOUND("ITD_UEC_1", "No such user found", HttpStatus.NOT_FOUND),
  USER_ALREADY_EXISTS("ITD_UEC_2", "User already exists", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

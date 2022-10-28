package com.rmr.dinosaurs.domain.auth.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TempConfirmationErrorCode implements ErrorCode {

  INVALID_CONFIRMATION_CODE("ITD_TCEC_1", "Invalid confirmation code provided",
      HttpStatus.BAD_REQUEST),
  CONFIRMATION_CODE_NOT_FOUND("ITD_TCEC_2", "No such confirmation code was found",
      HttpStatus.NOT_FOUND);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

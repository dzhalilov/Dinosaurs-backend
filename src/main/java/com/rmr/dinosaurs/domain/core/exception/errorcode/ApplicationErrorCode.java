package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

  WRONG_DATA_FORMAT("ITD_AEC_1", "Wrong data format", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("ITD_AEC_2", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
  UNSUPPORTED_MEDIA_TYPE("ITD_AEC_3", "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

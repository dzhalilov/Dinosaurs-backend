package com.rmr.dinosaurs.core.exception.errorcode;

import com.rmr.dinosaurs.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PageErrorCode implements ErrorCode {

  NEGATIVE_PAGE_NUMBER("ITD_PEC_1", "Negative page number", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

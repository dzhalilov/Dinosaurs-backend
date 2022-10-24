package com.rmr.dinosaurs.core.exception.errorcode;

import com.rmr.dinosaurs.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfessionErrorCode implements ErrorCode {

  PROFESSION_NOT_FOUND("ITD_PEC_1", "No such profession found", HttpStatus.NOT_FOUND);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

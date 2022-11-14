package com.rmr.dinosaurs.domain.statistics.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatisticsErrorCode implements ErrorCode {

  EXPORT_ERROR("ITD_SEC_1", "Can't export data to xlsx", HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

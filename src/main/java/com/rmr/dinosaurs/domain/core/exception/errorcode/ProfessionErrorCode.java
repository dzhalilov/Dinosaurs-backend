package com.rmr.dinosaurs.domain.core.exception.errorcode;

import com.rmr.dinosaurs.domain.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfessionErrorCode implements ErrorCode {

  PROFESSION_NOT_FOUND("ITD_PEC_1", "No such profession found", HttpStatus.NOT_FOUND),
  PROFESSION_ALREADY_EXISTS("ITD_PEC_2", "Profession with such name already exists",
      HttpStatus.BAD_REQUEST);

  private final String errorName;
  private final String message;
  private final HttpStatus httpStatus;

}

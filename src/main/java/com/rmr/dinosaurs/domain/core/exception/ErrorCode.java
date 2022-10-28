package com.rmr.dinosaurs.domain.core.exception;

import java.io.Serializable;
import org.springframework.http.HttpStatus;

public interface ErrorCode extends Serializable {

  String getErrorName();

  String getMessage();

  default HttpStatus getHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }

}

package com.rmr.dinosaurs.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(value = ServiceException.class)
  public ResponseEntity<ServiceException> serviceExceptionResponseEntity(
      ServiceException serviceException) {
    log.error(serviceException.getMessage(), serviceException);
    return ResponseEntity.status(serviceException.getErrorCode()
            .getHttpStatus())
        .body(serviceException);
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Exception> exceptionResponseEntity(
      Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .build();
  }

  @ExceptionHandler(value = {
      MethodArgumentTypeMismatchException.class,
      MethodArgumentNotValidException.class})
  public ResponseEntity<ServiceException> methodArgumentException(
      Exception exception) {
    log.debug(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<ServiceException> accessDeniedExceptionErrorHandler(
      Exception exception) {
    log.debug(exception.getMessage(), exception);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

}

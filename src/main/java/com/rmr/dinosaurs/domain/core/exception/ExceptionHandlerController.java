package com.rmr.dinosaurs.domain.core.exception;

import static com.rmr.dinosaurs.domain.auth.exception.errorcode.AuthErrorCode.ACCESS_DENIED_EXCEPTION;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ApplicationErrorCode.INTERNAL_SERVER_ERROR;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ApplicationErrorCode.UNSUPPORTED_MEDIA_TYPE;
import static com.rmr.dinosaurs.domain.core.exception.errorcode.ApplicationErrorCode.WRONG_DATA_FORMAT;

import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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

  @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Exception> notSupportedHttpMediaFormatExceptionHandler(
      Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(new ServiceException(UNSUPPORTED_MEDIA_TYPE));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<Exception> exceptionResponseEntity(
      Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ServiceException(INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(value = {
      MethodArgumentTypeMismatchException.class,
      MethodArgumentNotValidException.class,
      ValidationException.class,
      HttpMessageNotReadableException.class,
      MissingServletRequestParameterException.class,
      MissingPathVariableException.class,
      HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ServiceException> methodArgumentException(
      Exception exception) {
    log.debug(exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ServiceException(WRONG_DATA_FORMAT));
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public ResponseEntity<ServiceException> accessDeniedExceptionErrorHandler(
      Exception exception) {
    log.debug(exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(new ServiceException(ACCESS_DENIED_EXCEPTION));
  }

}

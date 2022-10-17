package com.rmr.dinosaurs.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"code", "message", "time"})
@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class ServiceException extends RuntimeException {

  public static final String DATE_TIME_FORMAT = "dd.MM.yy HH:mm:ss";

  @JsonIgnore
  private final ErrorCode errorCode;

  @JsonProperty("code")
  private final String code;

  @JsonProperty("time")
  private final String causedAt;

  public ServiceException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    code = errorCode.getErrorName();
    causedAt = getFormattedCurrentTime();
  }

  @Override
  public String getMessage() {
    return errorCode.getMessage();
  }

  private String getFormattedCurrentTime() {
    return LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
  }

}
